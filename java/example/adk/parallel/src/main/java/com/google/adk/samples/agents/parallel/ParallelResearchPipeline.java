package com.google.adk.samples.agents.parallel;

import com.google.adk.agents.SequentialAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.parallel.impl.RootAgentFactory;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

public class ParallelResearchPipeline {

    private static final String APP_NAME = "parallel_research_app";
    private static final String USER_ID = "research_user_01";

    public static void main(String[] args) {
        runAgent(RootAgentFactory.instantiate(), "Summarize recent sustainable tech advancements.");
    }

    public static void runAgent(SequentialAgent sequentialPipelineAgent, String query) {

        // Create an InMemoryRunner
        final InMemoryRunner runner = new InMemoryRunner(sequentialPipelineAgent, APP_NAME);

        // InMemoryRunner automatically creates a session service. Create a session using the service
        final Session session = runner.sessionService().createSession(APP_NAME, USER_ID).blockingGet();
        final Content userMessage = Content.fromParts(Part.fromText(query));

        // Run the agent
        final Flowable<Event> eventStream = runner.runAsync(USER_ID, session.id(), userMessage);

        // Stream event response
        eventStream.blockingForEach(
                event -> {
                    if (event.finalResponse()) {
                        System.out.printf("Event Author: %s \n Event Response: %s \n\n\n", event.author(), event.stringifyContent());
                    }
                }
        );

    }
}
