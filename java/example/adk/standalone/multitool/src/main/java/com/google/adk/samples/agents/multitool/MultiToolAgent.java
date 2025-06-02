package com.google.adk.samples.agents.multitool;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.multitool.agentbuilder.AgentBuilder;
import com.google.adk.samples.agents.multitool.agentrunner.MultiToolRunner;
import com.google.adk.samples.agents.multitool.tool.CurrentTimeTool;
import com.google.adk.samples.agents.multitool.tool.WeatherTool;
import com.google.adk.sessions.Session;
import org.iatevale.adk.common.console.Console;
import org.iatevale.adk.common.console.InputType;
import org.iatevale.adk.common.logger.AgentLogger;

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

        // Consola para interaccion con el usuario
        try (Console console = new Console(Constants.HELLO, Constants.PROMPT)) {
            while (switch (console.Input()) {
                case InputType.Quit quit -> false;
                case InputType.Empty empty -> true;
                case InputType.Prompt prompt -> multiToolRunner.execute(prompt.text(), console::output);
            }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}