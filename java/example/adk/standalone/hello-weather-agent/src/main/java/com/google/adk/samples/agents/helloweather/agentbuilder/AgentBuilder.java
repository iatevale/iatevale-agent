package com.google.adk.samples.agents.helloweather.agentbuilder;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.samples.agents.helloweather.tool.HelloWeatherTool;
import com.google.adk.tools.FunctionTool;

public class AgentBuilder {

    public static BaseAgent ROOT_AGENT = initAgent();

    private static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name("hello-weather-agent")
                .description("Hello World")
                .instruction("""
                You are a friendly assistant, answering questions in a concise manner.
                
                When asked about weather information, you MUST use the `getWeather` function.
                """)
                .model("gemini-2.0-flash")
                .tools(FunctionTool.create(HelloWeatherTool.class, "getWeather"))
                .build();
    }

}
