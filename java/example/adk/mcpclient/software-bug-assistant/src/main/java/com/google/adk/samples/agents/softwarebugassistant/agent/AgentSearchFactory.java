package com.google.adk.samples.agents.softwarebugassistant.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import org.iatevale.adk.common.model.GeminiModelUtil;

public record AgentSearchFactory(LlmAgent llmAgent) {

    private static final String AGENT_NAME = "google_search_agent";
    private static final String DESCRIPTION = "Search Google Search";
    private static final String INSTRUCTION = """
                You're a specialist in Google Search
            """;

    static public AgentSearchFactory instantiate() {
        final LlmAgent agent = LlmAgent.builder()
                .model(GeminiModelUtil.create())
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .instruction(INSTRUCTION)
                .tools(new GoogleSearchTool()) // Your Google search tool
                .outputKey("google_search_result")
                .build();
        return new AgentSearchFactory(agent);
    }

}
