package com.google.adk.samples.agents.multitool.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.BaseTool;
import org.iatevale.example.adk.common.model.AgentConfig;

public record AgentFactory(LlmAgent llmAgent) {

    private static final String AGENT_NAME = "multi_tool_agent";
    private static final String DESCRIPTION = "Agent to answer questions about the time and weather in a city.";
    private static final String INSTRUCTION = """
            You are a helpful llmAgent who can answer user questions about the time and weather in a city.
            """;

    static public AgentFactory instantiate(BaseTool... tools) {
        final LlmAgent agent = AgentConfig.apply(LlmAgent.builder())
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .instruction(INSTRUCTION)
                .tools(tools)
                .build();

        return new AgentFactory(agent);
    }

}
