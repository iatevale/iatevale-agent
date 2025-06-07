package org.iatevale.example.adk.agent.parallel.agent.phase1.parallel;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class RenewableEnergyResearcherFactory {

    static public LlmAgent instantiate(GoogleSearchTool googleSearchTool) {
        return LlmAgentFactory.child()
                .name("RenewableEnergyResearcher")
                .instruction("""
                     You are an AI Research Assistant specializing in energy.
                     Research the latest advancements in 'renewable energy sources'.
                     Use the Google Search tool provided.
                     Summarize your key findings concisely (1-2 sentences).
                     Output *only* the summary.
                     """)
                .description("Researches renewable energy sources.")
                .tools(googleSearchTool)
                .outputKey("renewable_energy_result") // Store result in state
                .build();
    }

}
