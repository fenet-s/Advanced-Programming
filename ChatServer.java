import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started... Waiting for client");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader input =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output =
                new PrintWriter(socket.getOutputStream(), true);

            BufferedReader keyboard =
                new BufferedReader(new InputStreamReader(System.in));

            String msg = input.readLine().trim();

            while (true) {
                if (input.ready()) {
                    msg = input.readLine();
                    System.out.println("Client: " + msg);
                }

                if (keyboard.ready()) {
                    msg = keyboard.readLine();
                    output.println("server"+msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
