package org.iatevale.example.adk.agent.loop.agent.phase2;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import org.iatevale.example.adk.agent.loop.agent.phase2.llm.Phase1CriticalFactory;
import org.iatevale.example.adk.agent.loop.agent.phase2.llm.Phase2RefinerFactory;
import org.iatevale.example.adk.agent.loop.tool.ExitLoopTool;

public class RefinementLoopFactory {

    static public LoopAgent instantiate() {

        final ExitLoopTool exitLoopTool = ExitLoopTool.instantiate();

        final LlmAgent phase1 = Phase1CriticalFactory.instantiate();
        final LlmAgent phase2 = Phase2RefinerFactory.instantiate(exitLoopTool.functionTool());

        return LoopAgent.builder()
                .name("RefinementLoop")
                .description("Repeatedly refines the document with critique and then exits.")
                .subAgents(phase1, phase2)
                .maxIterations(5)
                .build();

    }

}
