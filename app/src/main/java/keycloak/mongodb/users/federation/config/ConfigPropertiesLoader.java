package keycloak.mongodb.users.federation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropertiesLoader {
    private static ConfigPropertiesLoader configLoader;
    private Properties configs;

    public Properties getConfigs() {
        return configs;
    }

    public ConfigPropertiesLoader() {
        try {
            InputStream input = ConfigPropertiesLoader.class.getClassLoader().getResourceAsStream("application.properties");
            Properties prop = new Properties();
            prop.load(input);
            configs = prop;
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static ConfigPropertiesLoader getInstance() {
        if (configLoader == null) {
            configLoader = new ConfigPropertiesLoader();
        }
        return configLoader;
    }
}

