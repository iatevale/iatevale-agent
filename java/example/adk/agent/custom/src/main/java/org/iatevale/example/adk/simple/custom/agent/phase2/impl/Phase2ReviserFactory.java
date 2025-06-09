package org.iatevale.example.adk.simple.custom.agent.phase2.impl;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class Phase2ReviserFactory {
    static public LlmAgent instantiate() {
        return LlmAgentFactory.root()
                .name("Reviser")
                .description("Revises the story based on criticism.")
                .instruction(
                      """
                      You are a story reviser. Revise the story provided in
                      session state with key 'current_story', based on the criticism in
                      session state with key 'criticism'. Output only the revised story.
                      """)
                .inputSchema(null)
                .outputKey("current_story") // Overwrites the original story
                .build();
    }
}
