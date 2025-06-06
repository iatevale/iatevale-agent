package org.iatevale.example.adk.agent.sequential.impl;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import org.iatevale.example.adk.agent.sequential.impl.llm.Phase1CodeWriterFactory;
import org.iatevale.example.adk.agent.sequential.impl.llm.Phase2CodeReviewerFactory;
import org.iatevale.example.adk.agent.sequential.impl.llm.Phase3CodeRefactorerFactory;

import static org.iatevale.example.adk.agent.sequential.SequentialAgentExample.APP_NAME;

public class RootAgentFactory {

    static public SequentialAgent instantiate() {

        final LlmAgent codeWriterAgent = Phase1CodeWriterFactory.instantiate();
        final LlmAgent codeReviewerAgent = Phase2CodeReviewerFactory.instantiate();
        final LlmAgent codeRefactorerAgent = Phase3CodeRefactorerFactory.instantiate();

        return SequentialAgent.builder()
                .name(APP_NAME)
                .description("Executes a sequence of code writing, reviewing, and refactoring.")
                .subAgents(codeWriterAgent, codeReviewerAgent, codeRefactorerAgent)
                .build();

    }

}