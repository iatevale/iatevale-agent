package org.iatevale.example.adk.agent.loop.impl.phase2;

import com.google.adk.agents.LoopAgent;
import org.iatevale.example.adk.agent.loop.impl.phase2.llm.Phase1CriticalInLoopFactory;
import org.iatevale.example.adk.agent.loop.impl.phase2.llm.Phase2RefinerAgentFactory;
import org.iatevale.example.adk.agent.loop.tool.ExitLoopTool;

public class RefinementLoopFactory {

    static public LoopAgent instantiate() {

        final ExitLoopTool exitLoopTool = ExitLoopTool.instantiate();

        final Phase1CriticalInLoopFactory phase1 = Phase1CriticalInLoopFactory.instantiate();
        final Phase2RefinerAgentFactory phase2 = Phase2RefinerAgentFactory.instantiate(exitLoopTool.functionTool());

        return LoopAgent.builder()
                .name("RefinementLoop")
                .description("Repeatedly refines the document with critique and then exits.")
                .subAgents(phase1.llmAgent(), phase2.llmAgent())
                .maxIterations(5)
                .build();

    }

}
