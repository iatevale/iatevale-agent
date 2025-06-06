package org.iatevale.example.adk.agent.parallel.impl.phase1.parallel;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import org.iatevale.example.adk.common.model.AgentConfig;

public class EVResearcherFactory {

    static public LlmAgent instantiate(GoogleSearchTool googleSearchTool) {
        return AgentConfig.apply(LlmAgent.builder())
                .name("EVResearcher")
                .instruction("""
                     You are an AI Research Assistant specializing in transportation.
                     Research the latest developments in 'electric vehicle technology'.
                     Use the Google Search tool provided.
                     Summarize your key findings concisely (1-2 sentences).
                     Output *only* the summary.
                     """)
                .description("Researches electric vehicle technology.")
                .tools(googleSearchTool)
                .outputKey("ev_technology_result") // Store result in state
                .build();
    }

}
