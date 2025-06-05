package com.google.adk.samples.agents.helloweather.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.samples.agents.helloweather.tool.HelloWeatherToolFactory;
import org.iatevale.adk.common.model.AgentConfig;
import org.iatevale.config.AdkParameters;
import org.iatevale.config.IATevaleConfig;

public record AgentFactory(LlmAgent llmAgent) {

    private static final String AGENT_NAME = "hello-weather-llmAgent";
    private static final String DESCRIPTION = "Hello World";
    private static final String INSTRUCTION = """
                            You are a friendly assistant, answering questions in a concise manner.
            
                            When asked about weather information, you MUST use the `getWeather` function.
            """;

    public static AgentFactory instantiate(HelloWeatherToolFactory helloWeatherTool) {
        final AdkParameters adkParameters = IATevaleConfig.getAdkParameters();
        final LlmAgent agent = AgentConfig.apply(LlmAgent.builder())
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .instruction(INSTRUCTION)
                .tools(helloWeatherTool.functionTool())
                .build();
        return new AgentFactory(agent);
    }

}
