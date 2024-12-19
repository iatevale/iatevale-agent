package org.iatevale.app.helloworld;

import com.google.inject.Guice;
import com.google.inject.Stage;
import com.mycila.guice.ext.closeable.CloseableInjector;
import com.mycila.guice.ext.closeable.CloseableModule;
import org.iatevale.server.helloworldserver.HelloWorldServer;

import java.util.logging.Level;
import java.util.logging.LogManager;

public class HelloWorld {

    final static private String APP_NAME = "IAtevale Agent";

    static final public java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(HelloWorld.class.getName().replace("org.iatevale.", ""));

    static volatile private boolean finishServer = false;

    public static void main(String[] args) {

        try {

            // Se configura el logging
            LogManager.getLogManager().reset();
            LOGGER.info(String.format("Iniciando '%s' con JavaRuntime %s.", APP_NAME, System.getProperty("java.version")));

            // Se crea el inyector
            CloseableInjector closeableInjector = Guice.createInjector(
                    Stage.PRODUCTION,
                    new CloseableModule(),
                    new HelloWorldModule()
            ).getInstance(CloseableInjector.class);

            // Se planifica una tarea para cuando se reciba la senyal de parada
            Runtime.getRuntime().addShutdownHook(new Thread(new Finisher()));

            // Hay que arrancar el servidor
            // Al instalar el servidor se le inyectan todos los servicios que componen el entramado:
            //   * ..
            final HelloWorldServer helloWorldServer = closeableInjector.getInstance(HelloWorldServer.class);

            // El threda principal debe esperar a que se dentenga el Gateway
            LOGGER.info("El servidor se ha iniciado correctamente y ahora el thread principal entra en espera...");
            while (!finishServer) {
                synchronized (HelloWorld.class) {
                    try {
                        HelloWorld.class.wait(2000);
                    } catch (InterruptedException e) {
                    }
                }
            }

            // Se limpian los InjectCloseable
            closeableInjector.close();

            LOGGER.info("Este thread principal se detendra ahora!");

            // La parada ha sido ordenada
            System.exit(0);

        } catch (Throwable ex) {
            LOGGER.log(Level.SEVERE, "Se ha producido un error inesperado en el arranque del servidor !!!", ex);
            System.exit(1);
        }

    }

    static final class Finisher implements Runnable {

        @Override
        public void run() {

            HelloWorld.LOGGER.info("Se ha solicitado la parada ...");

            // Se despierta el thread principal para que evalue si tiene que parar
            synchronized (HelloWorld.class) {
                HelloWorld.finishServer = true;
                HelloWorld.class.notifyAll();
            }

        }

    }

}
