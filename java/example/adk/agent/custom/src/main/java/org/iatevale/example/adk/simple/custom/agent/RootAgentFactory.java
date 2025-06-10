package org.iatevale.example.adk.simple.custom.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import org.iatevale.example.adk.simple.custom.agent.impl.CustomAgentImpl;
import org.iatevale.example.adk.simple.custom.agent.phase1.StoreGeneratorFactory;
import org.iatevale.example.adk.simple.custom.agent.phase2.LoopAgentFactory;
import org.iatevale.example.adk.simple.custom.agent.phase3.SequentialAgentFactory;

public class RootAgentFactory {

    private static final String APP_NAME = "story_app";

    public static CustomAgentImpl instantiate() {

        final LlmAgent storyGenerator = StoreGeneratorFactory.instantiate();
        final LoopAgent loopAgent = LoopAgentFactory.instantiate();
        final SequentialAgent sequentialAgent = SequentialAgentFactory.instantiate();

        return new CustomAgentImpl(
                APP_NAME,
                storyGenerator,
                loopAgent,
                sequentialAgent
        );

    }
}
