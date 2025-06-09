package org.iatevale.example.adk.simple.custom.agent.phase1;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class StoreGeneratorFactory {

    static public LlmAgent instantiate() {
        return LlmAgentFactory.root()
                .name("StoryGenerator")
                .description("Generates the initial story.")
                .instruction(
                        """
                      You are a story writer. Write a short story (around 100 words) about a cat,
                      based on the topic provided in session state with key 'topic'
                      """)
                .inputSchema(null)
                .outputKey("current_story") // Key for storing output in session state
                .build();
    }
}
