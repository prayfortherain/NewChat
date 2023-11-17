import java.io.*;
import java.net.Socket;

public class Client {

    private static Socket clientSocket;

    public static void main(String[] args) {
        try {
            clientSocket = new Socket("localhost", 4004);

            Thread inputThread = new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    while (true) {
                        String serverMessage = in.readLine();
                        if (serverMessage == null || serverMessage.equals("выход")) {
                            break;
                        }
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Thread outputThread = new Thread(() -> {
                try {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        String clientMessage = reader.readLine();
                        if (clientMessage.equals("выход")) {
                            out.write(clientMessage);
                            break;
                        }
                        out.write("Клиент: " + clientMessage);
                        out.newLine();
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            inputThread.start();
            outputThread.start();

            inputThread.join();
            outputThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
