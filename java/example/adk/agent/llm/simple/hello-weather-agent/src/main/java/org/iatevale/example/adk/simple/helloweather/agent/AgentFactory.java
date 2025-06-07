package org.iatevale.example.adk.simple.helloweather.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import org.iatevale.config.AdkParameters;
import org.iatevale.config.IATevaleConfig;
import org.iatevale.example.adk.common.model.AgentConfig;
import org.iatevale.example.adk.simple.helloweather.tool.HelloWeatherToolFactory;

public class AgentFactory {

    private static final String AGENT_NAME = "hello-weather-llmAgent";
    private static final String DESCRIPTION = "Hello World";
    private static final String INSTRUCTION = """
                            You are a friendly assistant, answering questions in a concise manner.
            
                            When asked about weather information, you MUST use the `getWeather` function.
            """;

    public static LlmAgent instantiate(FunctionTool helloWeatherTool) {
        return AgentConfig.applyToRootModel(LlmAgent.builder())
                .name(AGENT_NAME)
                .description(DESCRIPTION)
                .instruction(INSTRUCTION)
                .tools(helloWeatherTool)
                .build();
    }

}
