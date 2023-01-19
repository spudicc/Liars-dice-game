package hr.algebra.java2.liars.dice.marina_spudic_java2.server;

import hr.algebra.java2.liars.dice.marina_spudic_java2.handlers.ClientSocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 1111;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        accept();
    }

    private static void accept() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true) {
                Socket socket = serverSocket.accept();
                System.err.println("Client connected from " + socket.getPort());
                ClientSocketHandler clientSocketHandler = new ClientSocketHandler(socket);
                new Thread(clientSocketHandler).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
