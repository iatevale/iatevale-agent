package org.iatevale.example.adk.agent.parallel.impl.phase1;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import org.iatevale.example.adk.agent.parallel.impl.phase1.parallel.CarbonCaptureResearcherFactory;
import org.iatevale.example.adk.agent.parallel.impl.phase1.parallel.EVResearcherFactory;
import org.iatevale.example.adk.agent.parallel.impl.phase1.parallel.RenewableEnergyResearcherFactory;
import com.google.adk.tools.GoogleSearchTool;

public class ParalleleFactory {

    static public ParallelAgent instantiate(GoogleSearchTool googleSearchTool) {

        final LlmAgent renewableEnergyResearcherAgent = RenewableEnergyResearcherFactory.instantiate(googleSearchTool);
        final LlmAgent evResearcherAgent = EVResearcherFactory.instantiate(googleSearchTool);
        final LlmAgent carbonCaptureResearcherAgent = CarbonCaptureResearcherFactory.instantiate(googleSearchTool);

        return ParallelAgent.builder()
                .name("ParallelWebResearchAgent")
                .subAgents(renewableEnergyResearcherAgent, evResearcherAgent, carbonCaptureResearcherAgent)
                .description("Runs multiple research agents in parallel to gather information.")
                .build();

    }

}
