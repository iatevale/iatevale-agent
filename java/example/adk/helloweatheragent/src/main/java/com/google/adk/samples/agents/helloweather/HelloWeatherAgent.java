package com.google.adk.samples.agents.helloweather;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.util.Map;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HelloWeatherAgent {

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
                .tools(FunctionTool.create(HelloWeatherAgent.class, "getWeather"))
                .build();
    }

    @Schema(description = "Get the weather forecast for a given city")
    public static Map<String, String> getWeather(
            @Schema(name = "city", description = "Name of the city to get the weather forecast for") String city) {
        return Map.of(
                "city", city,
                "forecast", "Sunny day, clear blue sky, temperature up to 24°C"
        );
    }

    public static void main(String[] args) {
        RunConfig runConfig = RunConfig.builder().build();
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);

        Session session = runner
                .sessionService()
                .createSession(runner.appName(), "user1234")
                .blockingGet();

        try (Scanner scanner = new Scanner(System.in, UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();
                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(session.userId(), session.id(), userMsg, runConfig);

                System.out.print("\nAgent > ");
                events.blockingForEach(event -> {
                    if (event.finalResponse()) {
                        System.out.println(event.stringifyContent());
                    }
                });
            }
        }
    }
}