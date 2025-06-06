package org.iatevale.example.adk.agent.parallel.impl;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.GoogleSearchTool;
import org.iatevale.example.adk.agent.parallel.impl.phase1.ParalleleFactory;
import org.iatevale.example.adk.agent.parallel.impl.phase2.SynthesisFactory;

public class RootAgentFactory {

     static public SequentialAgent instantiate() {

         // Tools
         final GoogleSearchTool googleSearchTool = new GoogleSearchTool();

         // Phases
         final ParallelAgent parallelAgents = ParalleleFactory.instantiate(googleSearchTool);
         final LlmAgent synthesisAgent = SynthesisFactory.instantiate();

         // Root agent
         return SequentialAgent.builder()
                 .name("ResearchAndSynthesisPipeline")
                 .subAgents(parallelAgents, synthesisAgent) // Run parallel research first, then merge
                 .description("Coordinates parallel research and synthesizes the results.")
                 .build();

     }
}
