package org.iatevale.example.adk.simple.helloweather.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class RootAgentFactory {

    public static LlmAgent instantiate(FunctionTool helloWeatherTool) {
        return LlmAgentFactory.root()
                .name("hello-weather-llmAgent")
                .description("Hello World")
                .instruction("""
                    You are a friendly assistant, answering questions in a concise manner.
    
                    When asked about weather information, you MUST use the `getWeather` function.
                    """)
                .tools(helloWeatherTool)
                .build();
    }

}
