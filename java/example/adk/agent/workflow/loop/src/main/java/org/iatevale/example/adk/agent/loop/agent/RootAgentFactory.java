package org.iatevale.example.adk.agent.loop.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import org.iatevale.example.adk.agent.loop.agent.phase1.InitialWriterFactory;
import org.iatevale.example.adk.agent.loop.agent.phase2.RefinementLoopFactory;

import static org.iatevale.example.adk.agent.loop.LoopAgentExample.APP_NAME;

public class RootAgentFactory {

    static public SequentialAgent instantiate() {

        // Se instancian los Llm agents
        final LlmAgent phase1 = InitialWriterFactory.instantiate();
        final LoopAgent phase2 = RefinementLoopFactory.instantiate();

        // Agente secuencial que orquesta el flujo principal.
        return SequentialAgent.builder()
                .name(APP_NAME)
                .description("Writes an initial document and then iteratively refines it with critique using an exit tool.")
                .subAgents(phase1, phase2)
                .build();

    }

}
