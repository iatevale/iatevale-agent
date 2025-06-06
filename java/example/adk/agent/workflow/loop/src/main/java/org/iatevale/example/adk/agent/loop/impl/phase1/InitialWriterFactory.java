package org.iatevale.example.adk.agent.loop.impl.phase1;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.AgentConfig;

import static com.google.adk.agents.LlmAgent.IncludeContents.NONE;

public class InitialWriterFactory {

    public static final String STATE_CURRENT_DOC = "current_document";

    static public LlmAgent instantiate() {

        return AgentConfig.apply(LlmAgent.builder())
                .name("InitialWriterAgent")
                .description(
                        "Writes the initial document draft based on the topic, aiming for some initial substance.")
                .instruction(
                        """
                            You are a Creative Writing Assistant tasked with starting a story.
                            Write the *first draft* of a short story (aim for 2-4 sentences).
                            Base the content *only* on the topic provided below. Try to introduce a specific element (like a character, a setting detail, or a starting action) to make it engaging.
        
                            Output *only* the story/document text. Do not add introductions or explanations.
                        """)
                .outputKey(STATE_CURRENT_DOC)
                .includeContents(NONE)
                .build();

    }

}
