import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket server;
    private static Socket clientSocket;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(4004);
            System.out.println("Сервер запущен!");

            clientSocket = server.accept();

            Thread inputThread = new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    while (true) {
                        String clientMessage = in.readLine();
                        if (clientMessage == null || clientMessage.equals("выход")) {
                            break;
                        }
                        System.out.println("Клиент: " + clientMessage);
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
                        String serverMessage = reader.readLine();
                        if (serverMessage.equals("выход")) {
                            out.write(serverMessage);
                            break;
                        }
                        out.write("Сервер: " + serverMessage);
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
                System.out.println("Сервер закрыт!");
                server.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
