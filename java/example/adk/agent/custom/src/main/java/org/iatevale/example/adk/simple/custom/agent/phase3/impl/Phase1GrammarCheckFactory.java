package org.iatevale.example.adk.simple.custom.agent.phase3.impl;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class Phase1GrammarCheckFactory {

    static public LlmAgent instantiate() {
        return LlmAgentFactory.root()
                .name("GrammarCheck")
                .description("Checks grammar and suggests corrections.")
                .instruction(
                        """
                       You are a grammar checker. Check the grammar of the story
                       provided in session state with key 'current_story'. Output only the suggested
                       corrections as a list, or output 'Grammar is good!' if there are no errors.
                       """)
                .outputKey("grammar_suggestions")
                .build();
    }

}
