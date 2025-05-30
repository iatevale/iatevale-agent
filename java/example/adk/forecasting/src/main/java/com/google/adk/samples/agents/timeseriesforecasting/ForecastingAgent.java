package com.google.adk.samples.agents.timeseriesforecasting;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.timeseriesforecasting.impl.Agent;
import com.google.adk.samples.agents.timeseriesforecasting.impl.RemoteConfig;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentConfigException;
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
import java.util.logging.Level;

/**
 * The main application class for the time series forecasting agent.
 */
public class ForecastingAgent {

    public static void main(String[] args) throws AgentException {

        final RemoteConfig remoteConfig = RemoteConfig.instantiate();
        final RemoteTools remoteTools = RemoteTools.instantiate(remoteConfig);
        final Agent agent = Agent.instantiate(remoteTools);
        final BaseAgent baseAgent = agent.getAgent();

        AgentLogger.setLevel(Level.WARNING);

        final InMemoryRunner runner = new InMemoryRunner(baseAgent);
        final Session session = runner.sessionService().createSession(
                baseAgent.name(),
                "tmp-user",
                (ConcurrentMap<String, Object>) null,
                (String) null
        ).blockingGet();

        runInteractiveSession(runner, session, baseAgent);

    }

    private static void runInteractiveSession(InMemoryRunner runner, Session session,
                                              BaseAgent agent) {
        System.out.println("\\nTime Series Forecasting Agent");
        System.out.println("-----------------------------");
        System.out.println("Examples:");
        System.out.println("predict next week's liquor sales in iowa");
        System.out.println("how many SF bike trips are expected tomorrow");
        System.out.println("forecast seattle air quality for the next 10 days");

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("\\nYou > ");
                String userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput.trim())) {
                    break;
                }
                if (userInput.trim().isEmpty()) {
                    continue;
                }

                Content userMsgForHistory = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runWithSessionId(session.id(), userMsgForHistory,
                        RunConfig.builder().build());

                System.out.print("\\nAgent > ");
                final StringBuilder agentResponseBuilder = new StringBuilder();
                final AtomicBoolean toolCalledInTurn = new AtomicBoolean(false);
                final AtomicBoolean toolErroredInTurn = new AtomicBoolean(false);

                events.blockingForEach(event -> processAgentEvent(event, agentResponseBuilder,
                        toolCalledInTurn, toolErroredInTurn));

                System.out.println();

                if (toolCalledInTurn.get() && !toolErroredInTurn.get()
                        && agentResponseBuilder.length() == 0) {
                    AgentLogger.warning("Agent used a tool but provided no text response.");
                } else if (toolErroredInTurn.get()) {
                    AgentLogger.warning(
                            "An error occurred during tool execution or in the agent's response processing.");
                }
            }
        }
        System.out.println("Exiting agent.");
    }

    private static void processAgentEvent(Event event, StringBuilder agentResponseBuilder,
                                          AtomicBoolean toolCalledInTurn, AtomicBoolean toolErroredInTurn) {
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