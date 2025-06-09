package org.iatevale.example.adk.simple.custom.agent.phase2;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import org.iatevale.example.adk.simple.custom.agent.phase2.impl.Phase1CriticFactory;
import org.iatevale.example.adk.simple.custom.agent.phase2.impl.Phase2ReviserFactory;

public class LoopAgentFactory {

    static public LoopAgent instantiate() {

        final LlmAgent phase1Critic = Phase1CriticFactory.instantiate();
        final LlmAgent phase2Reviser = Phase2ReviserFactory.instantiate();

        return LoopAgent.builder()
                .name("CriticReviserLoop")
                .description("Iteratively critiques and revises the story.")
                .subAgents(phase1Critic, phase2Reviser)
                .maxIterations(2)
                .build();
    }

}
