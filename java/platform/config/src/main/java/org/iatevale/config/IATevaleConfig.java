package org.iatevale.config;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class IATevaleConfig {

    static private Properties staticProperties;

    static private Properties getStaticProperties() {

        synchronized (IATevaleConfig.class) {

            // Si es la primera vez hay que cargar las propiedades
            if (staticProperties == null) {

                // Ruta al directorio de configuración en el home del usuario
                final String configFile = System.getProperty("user.home") + "/.iatevale/config.properties";

                // Cargar propiedades desde config.properties
                staticProperties = new Properties();

                try (FileInputStream propsFile = new FileInputStream(configFile)) {
                    staticProperties.load(propsFile);
                } catch (IOException e) {
                    throw new RuntimeException(String.format("No se ha podido cargar el fichero de configuracion '%s'", configFile), e);
                }

            }

            // Se retorna
            return staticProperties;

        }

    }

    static public GCloudAuthParameters getGCloudAuthParameters() throws IOException {

        final Properties properties = getStaticProperties();

        final String projectId = Objects.requireNonNull(properties.getProperty("google.cloud.project"), "No se ha podido cargar el id del proyecto de GCloud");
        final GoogleCredentials googleCredentials = Objects.requireNonNull(
                GoogleCredentials.fromStream(new FileInputStream(properties.getProperty("google.cloud.credentials")))
                        .createScoped("https://www.googleapis.com/auth/cloud-platform"),
                "No se ha podido cargar las credenciales de GCloud"
        );

        return new GCloudAuthParameters(projectId, googleCredentials);

    }

    static public String getTelegramToken() {
        final Properties properties = getStaticProperties();
        final String botToken = Objects.requireNonNull(properties.getProperty("telegram.bot.token"), "No se ha podido cargar el token de Telegram");
        return botToken;
    }

    static public void installGenAIKey() {
        final Properties properties = getStaticProperties();
        final String apiKey = Objects.requireNonNull(properties.getProperty("google.cloud.apikey"), "No se ha podido cargar la API Key de GCloud");
        System.setProperty("GOOGLE_API_KEY", apiKey);
        System.setProperty("GOOGLE_GENAI_USE_VERTEXAI", "FALSE");
    }

}
