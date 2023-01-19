package hr.algebra.java2.liars.dice.marina_spudic_java2.rmi;


import javax.naming.NamingException;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {
    private static final String PROVIDER_URL = "file:d:/";

    public static final String CONFIGURATION_FILENAME = "configuration.properties";
    private static final int RANDOM_PORT_HINT = 0;
    private static final String RMI_PORT_KEY = "rmi.port";

    public static void main(String[] args) {
        try {
            String rmiPort = JndiHelper.getValueFromConfiguration(RMI_PORT_KEY);
            Registry registry = LocateRegistry.createRegistry(Integer.parseInt(rmiPort));
            RemoteService remoteService = new RemoteServiceImplementation();
            RemoteService skeleton = (RemoteService) UnicastRemoteObject.exportObject(remoteService, RANDOM_PORT_HINT);
            registry.rebind(RemoteService.REMOTE_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry");
        } catch (NamingException | IOException e) {
            e.printStackTrace();
        }
    }

}
