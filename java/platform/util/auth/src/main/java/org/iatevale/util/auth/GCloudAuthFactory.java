package org.iatevale.util.auth;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class GCloudAuthFactory {

    static public GCloudAuthParameters getCredentials() throws IOException {
        final Properties properties = getProperties();
        return new GCloudAuthParameters(
                getProjectId(properties),
                getCredentials(properties)
        );
    }

    static private String getProjectId(Properties properties) {
        return properties.getProperty("project.id");
    }

    static private Credentials getCredentials(Properties properties) throws IOException {
        final String credentialsPath = properties.getProperty("credentials");
        return GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
    }

    static private Properties getProperties() throws IOException {

        // Ruta al directorio de configuración en el home del usuario
        final String configDir = System.getProperty("user.home") + "/.iatevale/config.properties";

        // Cargar propiedades desde config.properties
        final Properties configProps = new Properties();
        try (FileInputStream propsFile = new FileInputStream(configDir)) {
            configProps.load(propsFile);
            return configProps;
        }

    }

}
