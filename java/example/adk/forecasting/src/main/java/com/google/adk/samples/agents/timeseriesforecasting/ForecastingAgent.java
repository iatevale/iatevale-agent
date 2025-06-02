package com.google.adk.samples.agents.timeseriesforecasting;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.timeseriesforecasting.console.Console;
import com.google.adk.samples.agents.timeseriesforecasting.console.InputType;
import com.google.adk.samples.agents.timeseriesforecasting.impl.AgentBuilder;
import com.google.adk.samples.agents.timeseriesforecasting.impl.RemoteConfig;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentException;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentLogger;
import com.google.adk.samples.agents.timeseriesforecasting.impl.RemoteTools;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.FunctionResponse;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * The main application class for the time series forecasting agent.
 */
public class ForecastingAgent {

    final static private String HELLO = """
\\nTime Series Forecasting Agent
-----------------------------
Examples:
predict next week's liquor sales in iowa
how many SF bike trips are expected tomorrow
forecast seattle air quality for the next 10 days
""";

    final static private String PROMPT = "";

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
        ForecastingAgent forecastingAgent = new ForecastingAgent(runner, session);

        // Consola para interaccion con el usuario
        try (Console console = new Console(HELLO, PROMPT)) {
            while (switch (console.Input()) {
                    case InputType.Quit quit -> false;
                    case InputType.Empty qmpty -> true;
                    case InputType.Prompt prompt -> forecastingAgent.execute(prompt.text(), console::output);
                }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    final InMemoryRunner runner;
    final Session session;

    public ForecastingAgent(InMemoryRunner runner, Session session) {
        this.runner = runner;
        this.session = session;
    }

    private boolean execute(String prompt, Consumer<String> consoleOutput) {

        System.out.print("\\nYou > ");
        final Content userMsgForHistory = Content.fromParts(Part.fromText(prompt));
        final Flowable<Event> events = runner.runWithSessionId(
                session.id(),
                userMsgForHistory,
                RunConfig.builder().build()
        );

        consoleOutput.accept("\\nAgent > ");
        final StringBuilder agentResponseBuilder = new StringBuilder();
        final AtomicBoolean toolCalledInTurn = new AtomicBoolean(false);
        final AtomicBoolean toolErroredInTurn = new AtomicBoolean(false);

        events.blockingForEach(
                event -> processAgentEvent(event, agentResponseBuilder, toolCalledInTurn, toolErroredInTurn)
        );

        consoleOutput.accept("\\n");

        if (toolCalledInTurn.get() && !toolErroredInTurn.get() && agentResponseBuilder.isEmpty()) {
            AgentLogger.warning("Agent used a tool but provided no text response.");
        } else if (toolErroredInTurn.get()) {
            AgentLogger.warning("An error occurred during tool execution or in the agent's response processing.");
        }
        return true;
    }

    private static void processAgentEvent(

            Event event,
            StringBuilder agentResponseBuilder,
            AtomicBoolean toolCalledInTurn,
            AtomicBoolean toolErroredInTurn) {

        if (event.content().isPresent()) {

            event.content().get().parts().ifPresent(parts -> {
                for (Part part : parts) {
                    if (part.text().isPresent()) {
                        System.out.print(part.text().get());
                        agentResponseBuilder.append(part.text().get());
                    }
                    if (part.functionCall().isPresent()) {
                        toolCalledInTurn.set(true);
                    }
                    if (part.functionResponse().isPresent()) {
                        FunctionResponse fr = part.functionResponse().get();
                        fr.response().ifPresent(responseMap -> {
                            if (responseMap.containsKey("error")
                                    || (responseMap.containsKey("status")
                                    && "error".equalsIgnoreCase(
                                    String.valueOf(responseMap.get("status"))))) {
                                toolErroredInTurn.set(true);
                            }
                        });
                    }
                }
            });
        }
        if (event.errorCode().isPresent() || event.errorMessage().isPresent()) {
            toolErroredInTurn.set(true);
        }
    }

}