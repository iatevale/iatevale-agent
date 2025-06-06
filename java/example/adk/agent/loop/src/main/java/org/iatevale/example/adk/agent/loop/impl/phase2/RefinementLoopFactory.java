package org.iatevale.example.adk.agent.loop.impl.phase2;

import com.google.adk.agents.LoopAgent;
import org.iatevale.example.adk.agent.loop.impl.phase2.llm.Phase1CriticalFactory;
import org.iatevale.example.adk.agent.loop.impl.phase2.llm.Phase2RefinerFactory;
import org.iatevale.example.adk.agent.loop.tool.ExitLoopTool;

public class RefinementLoopFactory {

    static public LoopAgent instantiate() {

        final ExitLoopTool exitLoopTool = ExitLoopTool.instantiate();

        final Phase1CriticalFactory phase1 = Phase1CriticalFactory.instantiate();
        final Phase2RefinerFactory phase2 = Phase2RefinerFactory.instantiate(exitLoopTool.functionTool());

        return LoopAgent.builder()
                .name("RefinementLoop")
                .description("Repeatedly refines the document with critique and then exits.")
                .subAgents(phase1.llmAgent(), phase2.llmAgent())
                .maxIterations(5)
                .build();

    }

}
