import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);

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
                    System.out.println("Server: " + msg);
                }

                if (keyboard.ready()) {
                    msg = keyboard.readLine();
                    output.println("client:" +msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
