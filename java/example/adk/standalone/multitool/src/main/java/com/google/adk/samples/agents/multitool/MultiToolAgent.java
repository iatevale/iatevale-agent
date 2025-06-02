package com.google.adk.samples.agents.multitool;

import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.samples.agents.multitool.agentbuilder.AgentBuilder;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MultiToolAgent {

    final private static String USER_ID = "student";
    final private static String NAME = "multi_tool_agent";

    public static void main(String[] args) {
        final MultiToolAgent multiToolAgent = new MultiToolAgent();
        try (Scanner scanner = parseScanner()) {
            while (true) {
                if (!multiToolAgent.execute(scanner)) {
                    break;
                }
            }
        }
    }

    static private Scanner parseScanner() {
        return new Scanner(System.in, StandardCharsets.UTF_8);
    }

    final private InMemoryRunner runner;
    final private Session session;

    public MultiToolAgent() {
        runner = new InMemoryRunner(AgentBuilder.getAgent(NAME));
        session = runner
                .sessionService()
                .createSession(NAME, USER_ID)
                .blockingGet();
    }

    private boolean execute(Scanner scanner) {

        System.out.print("\nYou > ");
        String userInput = scanner.nextLine();
        if ("quit".equalsIgnoreCase(userInput)) {
            return false;
        }

        Content userMsg = Content.fromParts(Part.fromText(userInput));
        Flowable<Event> events = runner.runAsync(session.userId(), session.id(), userMsg);

        System.out.print("\nAgent > ");
        events.blockingForEach(event -> System.out.println(event.stringifyContent()));

        return true;

    }
}