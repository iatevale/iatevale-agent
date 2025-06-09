package org.iatevale.example.adk.simple.custom.agent.phase3;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.SequentialAgent;
import org.iatevale.example.adk.simple.custom.agent.phase3.impl.Phase1GrammarCheckFactory;
import org.iatevale.example.adk.simple.custom.agent.phase3.impl.Phase2ToneCheckFactory;

public class SequentialAgentFactory {

    static public SequentialAgent instantiate() {

        final LlmAgent grammarCheck = Phase1GrammarCheckFactory.instantiate();
        final LlmAgent toneCheck = Phase2ToneCheckFactory.instantiate();

        return SequentialAgent.builder()
                .name("PostProcessing")
                .description("Performs grammar and tone checks sequentially.")
                .subAgents(grammarCheck, toneCheck)
                .build();

    }

}
