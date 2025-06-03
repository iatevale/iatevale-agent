package com.google.adk.samples.agents.multitool.agentbuilder;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.BaseTool;
import org.iatevale.adk.common.tool.AbstractToolBuilder;

import java.util.Arrays;
import java.util.List;

public class AgentFactory {

    private static final String AGENT_NAME = "multi_tool_agent";
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String DESCRIPTION = "Agent to answer questions about the time and weather in a city.";
    private static final String INSTRUCTION = """
You are a helpful agent who can answer user questions about the time and weather in a city.
""";

    static public AgentFactory instantiate(AbstractToolBuilder...builders) {

        final List<BaseTool> tools = Arrays.stream(builders)
                .toList()
                .stream()
                .map(AbstractToolBuilder::getTool)
                .toList();

        final BaseAgent agent = LlmAgent.builder()
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .model(MODEL_NAME)
                .instruction(INSTRUCTION)
                .tools(tools)
                .build();

        return new AgentFactory(agent);

    }

    // Instancia

    final private BaseAgent agent;

    public AgentFactory(BaseAgent agent) {
        this.agent = agent;
    }

    public BaseAgent getAgent() {
        return agent;
    }

}
