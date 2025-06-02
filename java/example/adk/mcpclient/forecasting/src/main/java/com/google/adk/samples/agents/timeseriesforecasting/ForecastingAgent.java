package com.google.adk.samples.agents.timeseriesforecasting;

import com.google.adk.runner.InMemoryRunner;
import org.iatevale.adk.common.console.Console;
import org.iatevale.adk.common.console.InputType;
import com.google.adk.samples.agents.timeseriesforecasting.agentbuilder.AgentBuilder;
import com.google.adk.samples.agents.timeseriesforecasting.tool.RemoteConfig;
import com.google.adk.samples.agents.timeseriesforecasting.tool.RemoteTools;
import com.google.adk.samples.agents.timeseriesforecasting.agentrunner.ForecastingRunner;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentException;
import org.iatevale.adk.common.logger.AgentLogger;
import com.google.adk.sessions.Session;

import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

public class ForecastingAgent {

    final private static String USER_ID = "tmp-user";

    public static void main(String[] args) throws AgentException {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final RemoteConfig remoteConfig = RemoteConfig.instantiate();
        final RemoteTools remoteTools = RemoteTools.instantiate(remoteConfig);
        final AgentBuilder agentBuilder = AgentBuilder.instantiate(remoteTools);

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentBuilder.getAgent());

        // Se crea una sesión temporal para el agente
        final Session session = runner.sessionService()
                .createSession(
                    agentBuilder.getAgent().name(),
                    USER_ID,
                    (ConcurrentMap<String, Object>) null,
                    (String) null)
                .blockingGet();

        // Se crea el agente
        ForecastingRunner forecastingAgent = new ForecastingRunner(runner, session);

        // Consola para interaccion con el usuario
        try (Console console = new Console(Constants.HELLO, Constants.PROMPT)) {
            while (switch (console.Input()) {
                    case InputType.Quit quit -> false;
                    case InputType.Empty empty -> true;
                    case InputType.Prompt prompt -> forecastingAgent.execute(prompt.text(), console::output);
                }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}