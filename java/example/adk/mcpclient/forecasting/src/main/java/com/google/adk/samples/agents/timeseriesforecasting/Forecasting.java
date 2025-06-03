package com.google.adk.samples.agents.timeseriesforecasting;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.timeseriesforecasting.agent.AgentFactory;
import com.google.adk.samples.agents.timeseriesforecasting.runner.ForecastingRunner;
import com.google.adk.sessions.Session;
import org.iatevale.adk.common.console.ConsoleLoop;
import org.iatevale.adk.common.logger.AgentLogger;
import org.iatevale.adk.common.mcpclient.McpClientConfig;
import org.iatevale.adk.common.mcpclient.McpClientException;
import org.iatevale.adk.common.mcpclient.McpClientToolsFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Forecasting {

    final private static String USER_ID = "tmp-user";

    public static void main(String[] args) throws McpClientException {

        AgentLogger.setLevel(Level.WARNING);

        // Se ensambla el agente
        final McpClientConfig mcpClientConfig = McpClientConfig.instantiate();
        final McpClientToolsFactory mcpClientToolsFactory = McpClientToolsFactory.instantiate(mcpClientConfig);
        final AgentFactory agentFactory = AgentFactory.instantiate(mcpClientToolsFactory);

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentFactory.agent());

        // Se crea una sesión temporal para el agente
        final Session session = runner.sessionService()
                .createSession(
                    agentFactory.agent().name(),
                    USER_ID,
                    (ConcurrentMap<String, Object>) null,
                    (String) null)
                .blockingGet();

        // Se crea el agente
        final ForecastingRunner forecastingRunner = new ForecastingRunner(runner, session);
        final Forecasting forecasting = new Forecasting(forecastingRunner);

        // Consola para interaccion con el usuario
        ConsoleLoop.run(Constants.HELLO, Constants.PROMPT, forecasting::onInput);

    }

    final private ForecastingRunner forecastingRunner;

    public Forecasting(ForecastingRunner forecastingRunner) {
        this.forecastingRunner = forecastingRunner;
    }

    void onInput(String prompt, Consumer<String> consoleOutput) {
        forecastingRunner.execute(prompt, consoleOutput);
    }

}