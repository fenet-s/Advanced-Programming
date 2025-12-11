import java.io.*;
import java.net.*;
import javax.swing.*;

public class ServerUI {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("TCP Server Chat");
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

        // TCP Server Socket
        ServerSocket serverSocket = new ServerSocket(5000);
        chatArea.append("Server listening on port 5000...\n");

        Socket clientSocket = serverSocket.accept();
        chatArea.append("Client connected: " + clientSocket.getInetAddress() + "\n");

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // Listening thread
        Thread listenerThread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    chatArea.append("Client: " + msg + "\n");
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
                chatArea.append("Server: " + message + "\n");
                sendField.setText("");
            }
        });
    }
}
