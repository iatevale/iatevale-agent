package com.google.adk.samples.agents.parallel.phase1.llm;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import org.iatevale.adk.common.model.AgentConfig;

public class RenewableEnergyResearcherFactory {

    static public LlmAgent instantiate(GoogleSearchTool googleSearchTool) {
        return AgentConfig.apply(LlmAgent.builder())
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
