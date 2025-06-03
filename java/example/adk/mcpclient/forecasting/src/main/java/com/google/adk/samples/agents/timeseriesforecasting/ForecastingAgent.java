package com.google.adk.samples.agents.timeseriesforecasting;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.timeseriesforecasting.agentbuilder.AgentBuilder;
import com.google.adk.samples.agents.timeseriesforecasting.agentrunner.ForecastingRunner;
import com.google.adk.sessions.Session;
import org.iatevale.adk.common.console.ConsoleLoop;
import org.iatevale.adk.common.logger.AgentLogger;
import org.iatevale.adk.common.mcpclient.McpClientConfig;
import org.iatevale.adk.common.mcpclient.McpClientException;
import org.iatevale.adk.common.mcpclient.MscpClientTools;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.logging.Level;

public class ForecastingAgent {

    final private static String USER_ID = "tmp-user";

    public static void main(String[] args) throws McpClientException {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final McpClientConfig mcpClientConfig = McpClientConfig.instantiate();
        final MscpClientTools mscpClientTools = MscpClientTools.instantiate(mcpClientConfig);
        final AgentBuilder agentBuilder = AgentBuilder.instantiate(mscpClientTools);

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
        final ForecastingRunner forecastingRunner = new ForecastingRunner(runner, session);

        final ForecastingAgent forecastingAgent = new ForecastingAgent(forecastingRunner);

        // Consola para interaccion con el usuario
        ConsoleLoop.run(
                Constants.HELLO,
                Constants.PROMPT,
                forecastingAgent::onInput
        );

    }

    final private ForecastingRunner forecastingRunner;

    public ForecastingAgent(ForecastingRunner forecastingRunner) {
        this.forecastingRunner = forecastingRunner;
    }

    void onInput(String prompt, Consumer<String> consoleOutput) {
        forecastingRunner.execute(prompt, consoleOutput);
    }

}