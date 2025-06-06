package org.iatevale.example.adk.agent.loop.impl;

import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import org.iatevale.example.adk.agent.loop.impl.llm.CriticalInLoopFactory;
import org.iatevale.example.adk.agent.loop.impl.llm.InitialWriterFactory;
import org.iatevale.example.adk.agent.loop.impl.llm.RefinerAgentFactory;
import org.iatevale.example.adk.agent.loop.tool.ExitLoopTool;

public class RootAgentFactory {

    private static final String APP_NAME = "IterativeWritingPipeline";

    static public SequentialAgent instantiate() {

        // Se instancian las Tool
        final ExitLoopTool exitLoopTool = ExitLoopTool.instantiate();

        // Se instancian los Llm agents
        final InitialWriterFactory initialWriterFactory = InitialWriterFactory.instantiate();
        final CriticalInLoopFactory criticalInLoopFactory = CriticalInLoopFactory.instantiate();
        final RefinerAgentFactory refinerAgentFactory = RefinerAgentFactory.instantiate(exitLoopTool.functionTool());

        // Bucle de refiniamiento
        final LoopAgent refinementLoop = LoopAgent.builder()
                .name("RefinementLoop")
                .description("Repeatedly refines the document with critique and then exits.")
                .subAgents(criticalInLoopFactory.llmAgent(), refinerAgentFactory.llmAgent())
                .maxIterations(5)
                .build();

        // Agente secuencial que orquesta el flujo principal.
        return SequentialAgent.builder()
                .name(APP_NAME)
                .description("Writes an initial document and then iteratively refines it with critique using an exit tool.")
                .subAgents(initialWriterFactory.llmAgent(), refinementLoop)
                .build();

    }

}
