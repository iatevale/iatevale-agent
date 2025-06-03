package com.google.adk.samples.agents.helloweather;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.helloweather.agentbuilder.AgentBuilder;
import com.google.adk.samples.agents.helloweather.agentrunner.HelloWeatherRunner;
import com.google.adk.samples.agents.helloweather.tool.HelloWeatherTool;
import com.google.adk.sessions.Session;
import org.iatevale.adk.common.console.ConsoleLoop;
import org.iatevale.adk.common.logger.AgentLogger;

import java.util.function.Consumer;
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
        final HelloWeatherAgent helloWeatherAgent = new HelloWeatherAgent(helloWeatherRunner);

        // Consola para interaccion con el usuario
        ConsoleLoop.run(
                Constants.HELLO,
                Constants.PROMPT,
                helloWeatherAgent::onInput
        );

    }

    final HelloWeatherRunner helloWeatherRunner;

    public HelloWeatherAgent(HelloWeatherRunner helloWeatherRunner) {
        this.helloWeatherRunner = helloWeatherRunner;
    }

    void onInput(String prompt, Consumer<String> consoleOutput) {
        helloWeatherRunner.execute(prompt, consoleOutput);
    }

}