package hr.algebra.java2.liars.dice.marina_spudic_java2.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteService extends Remote{
    String REMOTE_OBJECT_NAME = "hr.algebra.java2.liars.dice.marina_spudic_java2.rmi";

    void sendMessage(String message, String playerName)  throws RemoteException;

    String receiveConversation()  throws RemoteException;
}
