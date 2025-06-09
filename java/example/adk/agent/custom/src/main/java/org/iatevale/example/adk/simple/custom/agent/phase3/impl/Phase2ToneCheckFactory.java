package org.iatevale.example.adk.simple.custom.agent.phase3.impl;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class Phase2ToneCheckFactory {

    static public LlmAgent instantiate() {
        return LlmAgentFactory.root()
                .name("ToneCheck")
                .description("Analyzes the tone of the story.")
                .instruction(
                        """
                      You are a tone analyzer. Analyze the tone of the story
                      provided in session state with key 'current_story'. Output only one word: 'positive' if
                      the tone is generally positive, 'negative' if the tone is generally negative, or 'neutral'
                      otherwise.
                      """)
                .outputKey("tone_check_result") // This agent's output determines the conditional flow
                .build();
    }

}
