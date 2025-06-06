package org.iatevale.example.adk.agent.sequential;

import com.google.adk.agents.SequentialAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import org.iatevale.example.adk.agent.sequential.impl.RootAgentFactory;

public class SequentialAgentExample {

    static final public String APP_NAME = "CodePipelineAgent";
    static final String USER_ID = "test_user_456";

    public static void main(String[] args) {
        SequentialAgentExample.runAgent(
                RootAgentFactory.instantiate(),
                "Write a Java function to calculate the factorial of a number."
        );
    }

    static private void runAgent(SequentialAgent rootAgent, String prompt) {

        // Create an InMemoryRunner
        final InMemoryRunner runner = new InMemoryRunner(rootAgent, APP_NAME);

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

}