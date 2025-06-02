package com.google.adk.samples.agents.timeseriesforecasting;

import com.google.adk.agents.BaseAgent;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.timeseriesforecasting.console.Console;
import com.google.adk.samples.agents.timeseriesforecasting.console.InputType;
import com.google.adk.samples.agents.timeseriesforecasting.impl.AgentBuilder;
import com.google.adk.samples.agents.timeseriesforecasting.impl.RemoteConfig;
import com.google.adk.samples.agents.timeseriesforecasting.impl.RemoteTools;
import com.google.adk.samples.agents.timeseriesforecasting.runner.ForecastingRunner;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentException;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentLogger;
import com.google.adk.sessions.Session;

import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;

public class ForecastingAgent {

    public static void main(String[] args) throws AgentException {

        AgentLogger.setLevel(Level.WARNING);

        // Se construye el agente
        final RemoteConfig remoteConfig = RemoteConfig.instantiate();
        final RemoteTools remoteTools = RemoteTools.instantiate(remoteConfig);
        final AgentBuilder agentBuilder = AgentBuilder.instantiate(remoteTools);

        final BaseAgent baseAgent = agentBuilder.getAgent();
        final InMemoryRunner runner = new InMemoryRunner(baseAgent);
        final Session session = runner.sessionService().createSession(
                baseAgent.name(),
                "tmp-user",
                (ConcurrentMap<String, Object>) null,
                (String) null
        ).blockingGet();

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