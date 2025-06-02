package com.google.adk.samples.agents.helloweather;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.helloweather.agentbuilder.AgentBuilder;
import com.google.adk.samples.agents.helloweather.agentrunner.HelloWeatherRunner;
import com.google.adk.samples.agents.helloweather.tool.HelloWeatherTool;
import org.iatevale.adk.common.logger.AgentLogger;
import com.google.adk.sessions.Session;
import org.iatevale.adk.common.console.Console;
import org.iatevale.adk.common.console.InputType;

import java.util.logging.Level;

public class HelloWeatherAgent {

    final private static String USER_ID = "user1234";

    public static void main(String[] args) {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final HelloWeatherTool helloWeatherTool = HelloWeatherTool.instantiate();
        final AgentBuilder agentBuilder = AgentBuilder.instantiate(helloWeatherTool);

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentBuilder.getAgent());

        // Se crea una sesión temporal para el agente
        final Session session =  runner.sessionService()
                .createSession(runner.appName(), USER_ID)
                .blockingGet();

        // Se crea el agente
        final HelloWeatherRunner helloWeatherRunner = new HelloWeatherRunner(runner, session);

        // Consola para interaccion con el usuario
        try (Console console = new Console(Constants.HELLO, Constants.PROMPT)) {
            while (switch (console.Input()) {
                case InputType.Quit quit -> false;
                case InputType.Empty empty -> true;
                case InputType.Prompt prompt -> helloWeatherRunner.execute(prompt.text(), console::output);
            }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}