package com.google.adk.samples.agents.multitool;

import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.multitool.impl.AgentBuilder;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MultiToolAgent {

    private static String USER_ID = "student";
    private static String NAME = "multi_tool_agent";

    public static void main(String[] args) throws Exception {

        InMemoryRunner runner = new InMemoryRunner(AgentBuilder.getAgent(NAME));

        Session session =
                runner
                        .sessionService()
                        .createSession(NAME, USER_ID)
                        .blockingGet();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(USER_ID, session.id(), userMsg);

                System.out.print("\nAgent > ");
                events.blockingForEach(event -> System.out.println(event.stringifyContent()));
            }
        }
    }
}