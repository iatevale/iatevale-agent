package com.google.adk.samples.agents.parallel.impl.phase1.parallel;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import org.iatevale.adk.common.model.AgentConfig;

public class CarbonCaptureResearcherFactory {

    static public LlmAgent instantiate(GoogleSearchTool googleSearchTool) {
        return AgentConfig.apply(LlmAgent.builder())
                .name("CarbonCaptureResearcher")
                .instruction("""
                     You are an AI Research Assistant specializing in climate solutions.
                     Research the current state of 'carbon capture methods'.
                     Use the Google Search tool provided.
                     Summarize your key findings concisely (1-2 sentences).
                     Output *only* the summary.
                     """)
                .description("Researches carbon capture methods.")
                .tools(googleSearchTool)
                .outputKey("carbon_capture_result") // Store result in state
                .build();
    }

}
