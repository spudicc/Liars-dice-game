package hr.algebra.java2.liars.dice.marina_spudic_java2.model;

import hr.algebra.java2.liars.dice.marina_spudic_java2.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private String message;
    private PlayerData playerData;

    private GameMetaData gameMetaData;

    public Client(PlayerData playerData) {
        try {
            this.playerData = playerData;
            this.socket = new Socket(Server.HOST, Server.PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.err.println("KLIJENT SE SPAJA NA " + socket.getInetAddress() + ":" + socket.getPort());
            oos.writeObject(playerData);

        } catch (IOException e) {
            e.printStackTrace();
            closeSocketAndStreams();
        }
    }

    public void listenForGameReady(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GameMetaData gameMetaData1;
                while (socket.isConnected()){
                    try {
                        System.out.println("i am in listen iz clienta");
                        gameMetaData1 = (GameMetaData) ois.readObject();
                        System.out.println(gameMetaData1);
                        gameMetaData = gameMetaData1;
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                GameMetaData opponentFinished;
                while (socket.isConnected()){
                    try {
                        opponentFinished = (GameMetaData) ois.readObject();
                        gameMetaData = opponentFinished;
                        System.out.println(gameMetaData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread2.start();

    }

    public GameMetaData getGameMetaData() {
        return gameMetaData;
    }

    public void setGameMetaData(GameMetaData gameMetaData) {
        this.gameMetaData = gameMetaData;
    }

    public String getMessage() {
        return message;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
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

    public void sendTurn() throws IOException {
        if (this.socket.isConnected()){
            gameMetaData.setPlayerOneTurn(!gameMetaData.isPlayerOneTurn());
            oos.writeObject(gameMetaData);
        }
    }
}
