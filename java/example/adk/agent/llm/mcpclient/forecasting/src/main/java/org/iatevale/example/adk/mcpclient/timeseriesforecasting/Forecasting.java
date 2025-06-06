package org.iatevale.example.adk.mcpclient.timeseriesforecasting;

import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import org.iatevale.example.adk.common.console.ConsoleLoop;
import org.iatevale.example.adk.common.logger.AgentLogger;
import org.iatevale.example.adk.common.mcpclient.McpClientConfig;
import org.iatevale.example.adk.common.mcpclient.McpClientException;
import org.iatevale.example.adk.common.mcpclient.McpClientToolsFactory;
import org.iatevale.example.adk.mcpclient.timeseriesforecasting.agent.AgentFactory;
import org.iatevale.example.adk.mcpclient.timeseriesforecasting.runner.ForecastingRunner;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Forecasting {

    final private static String USER_ID = "tmp-user";

    public static void main(String[] args) throws McpClientException {

        AgentLogger.setLevel(Level.WARNING);

        // Se cargan las tools remotas (Mcp client)
        final McpClientConfig mcpClientConfig = McpClientConfig.instantiate();
        final McpClientToolsFactory mcpClientToolsFactory = McpClientToolsFactory.instantiate(mcpClientConfig);
        final AgentFactory agentFactory = AgentFactory.instantiate(mcpClientToolsFactory);

        // Se crea en runner el agente, con las herramientas cargadas
        final InMemoryRunner runner = new InMemoryRunner(agentFactory.llmAgent());

        // Se crea una sesión temporal para el agente
        final Session session = runner.sessionService()
                .createSession(
                    agentFactory.llmAgent().name(),
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