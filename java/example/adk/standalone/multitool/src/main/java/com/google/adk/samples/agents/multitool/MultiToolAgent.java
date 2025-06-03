package com.google.adk.samples.agents.multitool;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.multitool.agentbuilder.AgentFactory;
import com.google.adk.samples.agents.multitool.agentrunner.MultiToolRunner;
import com.google.adk.samples.agents.multitool.tool.CurrentTimeFactory;
import com.google.adk.samples.agents.multitool.tool.WeatherFactory;
import com.google.adk.sessions.Session;
import org.iatevale.adk.common.console.ConsoleLoop;
import org.iatevale.adk.common.logger.AgentLogger;

import java.util.function.Consumer;
import java.util.logging.Level;

public class MultiToolAgent {

    final private static String USER_ID = "student";

    public static void main(String[] args) {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final CurrentTimeFactory currentTimeFactory = CurrentTimeFactory.instantiate();
        final WeatherFactory weatherFactory = WeatherFactory.instantiate();
        final AgentFactory agentFactory = AgentFactory.instantiate(currentTimeFactory, weatherFactory);

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentFactory.agent());

        // Se crea una sesión temporal para el agente
        final Session session = runner.sessionService()
                .createSession(runner.appName(), USER_ID)
                .blockingGet();

        // Se crea el agente
        final MultiToolRunner multiToolRunner = new MultiToolRunner(runner, session);
        final MultiToolAgent multiToolAgent = new MultiToolAgent(multiToolRunner);

        // Consola para interaccion con el usuario
        ConsoleLoop.run(Constants.HELLO, Constants.PROMPT, multiToolAgent::onInput);

    }

    final MultiToolRunner multiToolRunner;

    public MultiToolAgent(MultiToolRunner multiToolRunner) {
        this.multiToolRunner = multiToolRunner;
    }

    void onInput(String prompt, Consumer<String> consoleOutput) {
        multiToolRunner.execute(prompt, consoleOutput);
    }

}