package org.iatevale.config;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class IATevaleConfig {

    static private Properties properties;

    static private Properties getProperties() {

        synchronized (IATevaleConfig.class) {

            // Si es la primera vez hay que cargar las propiedades
            if (properties == null) {

                // Ruta al directorio de configuración en el home del usuario
                final String configFile = System.getProperty("user.home") + "/.iatevale/config.properties";

                // Cargar propiedades desde config.properties
                properties = new Properties();

                try (FileInputStream propsFile = new FileInputStream(configFile)) {
                    properties.load(propsFile);
                } catch (IOException e) {
                    throw new RuntimeException(String.format("No se ha podido cargar el fichero de configuracion '%s'", configFile), e);
                }

            }

            // Se retorna
            return properties;

        }

    }

    static public GCloudAuthParameters getGCloudAuthParameters() throws IOException {

        final Properties properties = getProperties();

        final String projectId = Objects.requireNonNull(properties.getProperty("gcloud.project_id"), "No se ha podido cargar el id del proyecto de GCloud");
        final GoogleCredentials googleCredentials = Objects.requireNonNull(
                GoogleCredentials.fromStream(new FileInputStream(properties.getProperty("gcloud.credentials")))
                        .createScoped("https://www.googleapis.com/auth/cloud-platform"),
                "No se ha podido cargar las credenciales de GCloud"
        );

        return new GCloudAuthParameters(projectId, googleCredentials);

    }

    static public String getTelegramToken() {
        final Properties properties = getProperties();
        final String botToken = Objects.requireNonNull(properties.getProperty("telegram.boot_token"), "No se ha podido cargar el token de Telegram");
        return botToken;
    }

}
