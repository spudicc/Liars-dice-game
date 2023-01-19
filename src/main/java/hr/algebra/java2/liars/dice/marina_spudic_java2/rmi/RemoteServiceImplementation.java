package hr.algebra.java2.liars.dice.marina_spudic_java2.rmi;

import java.rmi.RemoteException;

public class RemoteServiceImplementation implements RemoteService{
    private String chatHistory;

    public RemoteServiceImplementation() {
        chatHistory = "";
    }

    @Override
    public void sendMessage(String message, String playerName) throws RemoteException{
        chatHistory += playerName + " : " + message + "\n";
    }

    @Override
    public String receiveConversation() throws RemoteException{
        return chatHistory;
    }
}
