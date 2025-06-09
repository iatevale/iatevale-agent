package org.iatevale.example.adk.simple.custom.agent.phase2.impl;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class Phase1CriticFactory {

    static public LlmAgent instantiate() {
        return LlmAgentFactory.root()
                .name("Critic")
                .description("Critiques the story.")
                .instruction(
                        """
                      You are a story critic. Review the story provided in
                      session state with key 'current_story'. Provide 1-2 sentences of constructive criticism
                      on how to improve it. Focus on plot or character.
                      """)
                .inputSchema(null)
                .outputKey("criticism") // Key for storing criticism in session state
                .build();
    }

}
