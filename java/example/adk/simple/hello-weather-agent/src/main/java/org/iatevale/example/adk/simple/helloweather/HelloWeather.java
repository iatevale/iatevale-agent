package org.iatevale.example.adk.simple.helloweather;

import com.google.adk.runner.InMemoryRunner;
import org.iatevale.example.adk.simple.helloweather.agent.AgentFactory;
import org.iatevale.example.adk.simple.helloweather.runner.HelloWeatherRunner;
import org.iatevale.example.adk.simple.helloweather.tool.HelloWeatherToolFactory;
import com.google.adk.sessions.Session;
import org.iatevale.example.adk.common.console.ConsoleLoop;
import org.iatevale.example.adk.common.logger.AgentLogger;

import java.util.function.Consumer;
import java.util.logging.Level;

public class HelloWeather {

    final private static String USER_ID = "user1234";

    public static void main(String[] args) {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final HelloWeatherToolFactory helloWeatherTool = HelloWeatherToolFactory.instantiate();
        final AgentFactory agentFactory = AgentFactory.instantiate(helloWeatherTool);

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentFactory.llmAgent());

        // Se crea una sesión temporal para el agente
        final Session session =  runner.sessionService()
                .createSession(runner.appName(), USER_ID)
                .blockingGet();

        // Se crea el agente
        final HelloWeatherRunner helloWeatherRunner = new HelloWeatherRunner(runner, session);
        final HelloWeather helloWeather = new HelloWeather(helloWeatherRunner);

        // Consola para interaccion con el usuario
        ConsoleLoop.run(Constants.HELLO, Constants.PROMPT, helloWeather::onInput);

    }

    final HelloWeatherRunner helloWeatherRunner;

    public HelloWeather(HelloWeatherRunner helloWeatherRunner) {
        this.helloWeatherRunner = helloWeatherRunner;
    }

    void onInput(String prompt, Consumer<String> consoleOutput) {
        helloWeatherRunner.execute(prompt, consoleOutput);
    }

}