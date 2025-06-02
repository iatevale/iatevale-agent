package com.google.adk.samples.agents.helloweather.agentbuilder;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.samples.agents.helloweather.tool.HelloWeatherTool;

public class AgentBuilder {

    private static final String AGENT_NAME = "hello-weather-agent";
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String DESCRIPTION = "Hello World";
    private static final String INSTRUCTION = """
                You are a friendly assistant, answering questions in a concise manner.
                
                When asked about weather information, you MUST use the `getWeather` function.
""";

    public static AgentBuilder instantiate(HelloWeatherTool helloWeatherTool) {
        final BaseAgent agent = LlmAgent.builder()
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .model(MODEL_NAME)
                .instruction(INSTRUCTION)
                .tools(helloWeatherTool.getTool())
                .build();
        return new AgentBuilder(agent);
    }

    // Instancia

    final private BaseAgent agent;

    public AgentBuilder(BaseAgent agent) {
        this.agent = agent;
    }

    public BaseAgent getAgent() {
        return agent;
    }

}
