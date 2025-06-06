package com.google.adk.samples.agents.loop;

import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.loop.llm.CriticalInLoopFactory;
import com.google.adk.samples.agents.loop.llm.InitialWriterFactory;
import com.google.adk.samples.agents.loop.llm.RefinerAgentFactory;
import com.google.adk.samples.agents.loop.tool.ExitLoopTool;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

public class LoopAgentExample {

    private static final String APP_NAME = "IterativeWritingPipeline";
    private static final String USER_ID = "test_user_456";

    public static void main(String[] args) {
        LoopAgentExample loopAgentExample = new LoopAgentExample();
//        loopAgentExample.runAgent("Write a document about a woman named Alice.");
        loopAgentExample.runInititalWriter("Write a document about a woman named Alice.");
    }

    public void runAgent(String prompt) {

        // Se instancian las Tool
        final ExitLoopTool exitLoopTool = ExitLoopTool.instantiate();

        // Se instancian los Llm agents
        final InitialWriterFactory initialWriterFactory = InitialWriterFactory.instantiate();
        final CriticalInLoopFactory criticalInLoopFactory = CriticalInLoopFactory.instantiate();
        final RefinerAgentFactory refinerAgentFactory = RefinerAgentFactory.instantiate(exitLoopTool.functionTool());

        // Bucle de refiniamiento
        final LoopAgent refinementLoop = LoopAgent.builder()
                .name("RefinementLoop")
                .description("Repeatedly refines the document with critique and then exits.")
                .subAgents(criticalInLoopFactory.llmAgent(), refinerAgentFactory.llmAgent())
                .maxIterations(5)
                .build();

        // Agente secuencial que orquesta el flujo principal.
        final SequentialAgent iterativeWriterAgent = SequentialAgent.builder()
                .name(APP_NAME)
                .description("Writes an initial document and then iteratively refines it with critique using an exit tool.")
                .subAgents(initialWriterFactory.llmAgent(), refinementLoop)
                .build();

        // Create an InMemoryRunner
        final InMemoryRunner runner = new InMemoryRunner(iterativeWriterAgent, APP_NAME);

        // InMemoryRunner automatically creates a session service. Create a session using the service
        final Session session = runner.sessionService().createSession(APP_NAME, USER_ID).blockingGet();
        final Content userMessage = Content.fromParts(Part.fromText(prompt));

        // Run the agent
        final Flowable<Event> eventStream = runner.runAsync(USER_ID, session.id(), userMessage);

        // Stream event response
        eventStream.blockingForEach(
            event -> {
                if (event.finalResponse()) {
                    System.out.println(event.stringifyContent());
                }
            }
        );

    }

    public void runInititalWriter(String prompt) {

        final InitialWriterFactory initialWriterFactory = InitialWriterFactory.instantiate();

        final InMemoryRunner runner = new InMemoryRunner(initialWriterFactory.llmAgent(), APP_NAME);

        // InMemoryRunner automatically creates a session service. Create a session using the service
        final Session session = runner.sessionService().createSession(APP_NAME, USER_ID).blockingGet();
        final Content userMessage = Content.fromParts(Part.fromText(prompt));

        // Run the agent
        final Flowable<Event> eventStream = runner.runAsync(USER_ID, session.id(), userMessage);

        // Stream event response
        eventStream.blockingForEach(
                event -> {
                    if (event.finalResponse()) {
                        System.out.println(event.stringifyContent());
                    }
                });

    }
}