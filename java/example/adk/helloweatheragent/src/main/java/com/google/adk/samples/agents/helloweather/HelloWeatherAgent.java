package com.google.adk.samples.agents.helloweather;

import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.helloweather.impl.AgentBuilder;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HelloWeatherAgent {

    public static void main(String[] args) {
        final HelloWeatherAgent helloWeatherAgent = new HelloWeatherAgent();
        try (Scanner scanner = parseScanner()) {
            while (true) {
                if (!helloWeatherAgent.execute(scanner)) {
                    break;
                }
            }
        }
    }

    static private Scanner parseScanner() {
        return new Scanner(System.in, UTF_8);
    }

    final RunConfig runConfig;
    final InMemoryRunner runner;
    final Session session;

    private HelloWeatherAgent() {
        runConfig = RunConfig.builder().build();
        runner = new InMemoryRunner(AgentBuilder.ROOT_AGENT);
        session = runner
                .sessionService()
                .createSession(runner.appName(), "user1234")
                .blockingGet();
    }

    private boolean execute(Scanner scanner) {

        System.out.print("\nYou > ");
        String userInput = scanner.nextLine();
        if ("quit".equalsIgnoreCase(userInput)) {
            return false;
        }

        Content userMsg = Content.fromParts(Part.fromText(userInput));
        Flowable<Event> events = runner.runAsync(session.userId(), session.id(), userMsg, runConfig);

        System.out.print("\nAgent > ");
        events.blockingForEach(event -> {
            if (event.finalResponse()) {
                System.out.println(event.stringifyContent());
            }
        });

        return true;

    }

}