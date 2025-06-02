package com.google.adk.samples.agents.timeseriesforecasting.runner;

import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import org.iatevale.adk.common.logger.AgentLogger;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.FunctionResponse;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class ForecastingRunner {

    final private InMemoryRunner runner;
    final private Session session;
    final private RunConfig runConfig;

    public ForecastingRunner(InMemoryRunner runner, Session session) {
        this.runner = runner;
        this.session = session;
        this.runConfig = RunConfig.builder().build();
    }

    public boolean execute(String prompt, Consumer<String> consoleOutput) {

        System.out.print("\\nYou > ");
        final Content userMsgForHistory = Content.fromParts(Part.fromText(prompt));
        final Flowable<Event> events = runner.runWithSessionId(
                session.id(),
                userMsgForHistory,
                runConfig
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

    private void processAgentEvent(

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
