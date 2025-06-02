package com.google.adk.samples.agents.multitool.impl;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;

public class AgentBuilder {

    private static BaseAgent ROOT_AGENT;

    // The run your agent with Dev UI, the ROOT_AGENT should be a global public static variable.
    public static BaseAgent getAgent(String name    ) {
        synchronized (AgentBuilder.class) {
            if (ROOT_AGENT == null) {
                ROOT_AGENT = initAgent(name);
            }
        }
        return ROOT_AGENT;
    }

    public static BaseAgent initAgent(String name) {
        return LlmAgent.builder()
                .name(name)
                .model("gemini-2.0-flash")
                .description("Agent to answer questions about the time and weather in a city.")
                .instruction("You are a helpful agent who can answer user questions about the time and weather in a city.")
                .tools(
                        FunctionTool.create(CurrentTimeTool.class, "getCurrentTime"),
                        FunctionTool.create(WeatherTool.class, "getWeather")
                )
                .build();
    }

}
