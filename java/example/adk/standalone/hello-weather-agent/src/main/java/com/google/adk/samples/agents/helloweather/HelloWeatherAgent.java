package com.google.adk.samples.agents.helloweather;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.helloweather.agentbuilder.AgentBuilder;
import com.google.adk.samples.agents.helloweather.agentrunner.HelloWeatherRunner;
import org.iatevale.adk.common.logger.AgentLogger;
import com.google.adk.sessions.Session;
import org.iatevale.adk.common.console.Console;
import org.iatevale.adk.common.console.InputType;

import java.util.logging.Level;

public class HelloWeatherAgent {

    public static void main(String[] args) {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final InMemoryRunner runner = new InMemoryRunner(AgentBuilder.ROOT_AGENT);

        // Se crea una sesión temporal para el agente
        final Session session =  runner.sessionService()
                .createSession(runner.appName(), "user1234")
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