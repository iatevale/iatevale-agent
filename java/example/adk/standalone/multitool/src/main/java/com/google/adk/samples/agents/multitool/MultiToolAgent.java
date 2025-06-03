package com.google.adk.samples.agents.multitool;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.multitool.agentbuilder.AgentBuilder;
import com.google.adk.samples.agents.multitool.agentrunner.MultiToolRunner;
import com.google.adk.samples.agents.multitool.tool.CurrentTimeTool;
import com.google.adk.samples.agents.multitool.tool.WeatherTool;
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
        final CurrentTimeTool currentTimeTool = CurrentTimeTool.instantiate();
        final WeatherTool weatherTool = WeatherTool.instantiate();
        final AgentBuilder agentBuilder = AgentBuilder.instantiate(currentTimeTool, weatherTool);

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentBuilder.getAgent());

        // Se crea una sesión temporal para el agente
        final Session session = runner.sessionService()
                .createSession(runner.appName(), USER_ID)
                .blockingGet();

        // Se crea el agente
        final MultiToolRunner multiToolRunner = new MultiToolRunner(runner, session);
        final MultiToolAgent multiToolAgent = new MultiToolAgent(multiToolRunner);

        // Consola para interaccion con el usuario
        ConsoleLoop.run(
                Constants.HELLO,
                Constants.PROMPT,
                multiToolAgent::onInput
        );

    }

    final MultiToolRunner multiToolRunner;

    public MultiToolAgent(MultiToolRunner multiToolRunner) {
        this.multiToolRunner = multiToolRunner;
    }

    void onInput(String prompt, Consumer<String> consoleOutput) {
        multiToolRunner.execute(prompt, consoleOutput);
    }

}