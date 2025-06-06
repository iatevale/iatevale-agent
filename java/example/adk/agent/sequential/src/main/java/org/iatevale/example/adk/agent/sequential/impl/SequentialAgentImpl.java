package org.iatevale.example.adk.agent.sequential.impl;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import org.iatevale.example.adk.agent.sequential.impl.llm.Phase3CodeRefactorerFactory;
import org.iatevale.example.adk.agent.sequential.impl.llm.Phase2CodeReviewerFactory;
import org.iatevale.example.adk.agent.sequential.impl.llm.Phase1CodeWriterFactory;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

public class SequentialAgentImpl {

    private static final String APP_NAME = "CodePipelineAgent";
    private static final String USER_ID = "test_user_456";

    static public void runAgent(String prompt) {

        final LlmAgent codeWriterAgent = Phase1CodeWriterFactory.instantiate();
        final LlmAgent codeReviewerAgent = Phase2CodeReviewerFactory.instantiate();
        final LlmAgent codeRefactorerAgent = Phase3CodeRefactorerFactory.instantiate();

        final SequentialAgent codePipelineAgent = SequentialAgent.builder()
                .name(APP_NAME)
                .description("Executes a sequence of code writing, reviewing, and refactoring.")
                .subAgents(codeWriterAgent, codeReviewerAgent, codeRefactorerAgent)
                .build();

        // Create an InMemoryRunner
        final InMemoryRunner runner = new InMemoryRunner(codePipelineAgent, APP_NAME);

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