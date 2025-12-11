import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClientUI {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("TCP Client Chat");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(20, 20, 440, 200);
        frame.add(scrollPane);

        JTextField sendField = new JTextField();
        sendField.setBounds(20, 240, 300, 30);
        frame.add(sendField);

        JButton sendBtn = new JButton("Send");
        sendBtn.setBounds(360, 240, 100, 30);
        frame.add(sendBtn);

        frame.setVisible(true);

        // Connect to server
        Socket socket = new Socket("127.0.0.1", 5000);
        chatArea.append("Connected to server\n");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Listening thread
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    chatArea.append("Server: " + msg + "\n");
                }
            } catch (IOException e) {
                chatArea.append("Connection closed.\n");
            }
        });
        listenerThread.start();

        // Send button
        sendBtn.addActionListener(e -> {
            String message = sendField.getText();
            if (!message.isEmpty()) {
                out.println(message);
                chatArea.append("Client: " + message + "\n");
                sendField.setText("");
            }
        });
    }
}
