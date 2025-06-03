package com.google.adk.samples.agents.softwarebugassistant.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;

public record AgentSearchFactory(LlmAgent llmAgent) {

    private static final String AGENT_NAME = "google_search_agent";
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String DESCRIPTION = "Search Google Search";
    private static final String INSTRUCTION = """
                You're a specialist in Google Search
            """;

    static public AgentSearchFactory instantiate() {
        final LlmAgent agent = LlmAgent.builder()
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .model(MODEL_NAME)
                .instruction(INSTRUCTION)
                .tools(new GoogleSearchTool()) // Your Google search tool
                .outputKey("google_search_result")
                .build();
        return new AgentSearchFactory(agent);
    }

}
