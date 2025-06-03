package com.google.adk.samples.agents.multitool.agent;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.BaseTool;

public record AgentFactory(BaseAgent agent) {

    private static final String AGENT_NAME = "multi_tool_agent";
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String DESCRIPTION = "Agent to answer questions about the time and weather in a city.";
    private static final String INSTRUCTION = """
            You are a helpful agent who can answer user questions about the time and weather in a city.
            """;

    static public AgentFactory instantiate(BaseTool... tools) {
        final BaseAgent agent = LlmAgent.builder()
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .model(MODEL_NAME)
                .instruction(INSTRUCTION)
                .tools(tools)
                .build();

        return new AgentFactory(agent);
    }

}
