package hr.algebra.java2.liars.dice.marina_spudic_java2.rmi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class JndiHelper {
    private static InitialContext initialContext;
    private static final String PROVIDER_URL = "file:d:/";
    public static final String CONFIGURATION_FILENAME = "configuration.properties";

    private static InitialContext getInitialContext(){
        if (initialContext == null){
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            properties.setProperty(Context.PROVIDER_URL, PROVIDER_URL);

            try {
                initialContext = new InitialContext(properties);
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return initialContext;
    }

    public static String getValueFromConfiguration(String key) throws NamingException, IOException {
        Object configurationFileNameObject = getInitialContext().lookup(CONFIGURATION_FILENAME);
        Properties configurationProps = new Properties();
        configurationProps.load(new FileReader(configurationFileNameObject.toString()));
        return configurationProps.getProperty(key);
    }

}
