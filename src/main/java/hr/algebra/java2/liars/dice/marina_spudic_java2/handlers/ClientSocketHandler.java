package hr.algebra.java2.liars.dice.marina_spudic_java2.handlers;

import hr.algebra.java2.liars.dice.marina_spudic_java2.model.GameMetaData;
import hr.algebra.java2.liars.dice.marina_spudic_java2.model.PlayerData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSocketHandler implements Runnable{

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private PlayerData playerData;

    private static GameMetaData gameMetaData = new GameMetaData();

    public static List<ClientSocketHandler> listOfPlayers = new ArrayList<>();

    public ClientSocketHandler(Socket socket) {
        try {
            if (listOfPlayers.size() == 2)
            {
                socket.close();
                System.err.println("Too many players sorry");
                return;
            }
            this.socket = socket;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            this.playerData = (PlayerData) ois.readObject();

            if (listOfPlayers.size() == 0) {
                System.out.println(playerData + " from client handler first if");
                gameMetaData.setPlayerOneData(this.playerData);
            }

            listOfPlayers.add(this);

            if (listOfPlayers.size() == 2){
                System.out.println(playerData + " from client handler second if");
                gameMetaData.setPlayerTwoData(this.playerData);
                broadcastGameReady();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            closeSocketAndStreams();
        }
        System.out.println("Client from " + socket.getPort() + " conntecetededd");
    }

    private void broadcastGameReady() {
        for (ClientSocketHandler clientsockethandler:listOfPlayers) {
            try {
                clientsockethandler.oos.writeObject(gameMetaData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()){
            GameMetaData gameMetaData;

            try {
                System.out.println("Im in run in clientsockethandelr");
                gameMetaData = (GameMetaData) ois.readObject();
                for (ClientSocketHandler clientsockethandler:listOfPlayers) {
                    if (clientsockethandler.playerData.getPlayerId() != this.playerData.getPlayerId()){
                        System.out.println("uso sam u if");
                        clientsockethandler.oos.writeObject(gameMetaData);
                    }
                }
                System.out.println(gameMetaData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }
    }

    private void closeSocketAndStreams() {
        try {
            if (socket != null){
                socket.close();
            }
            if (ois != null){
                ois.close();
            }
            if (oos != null){
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
