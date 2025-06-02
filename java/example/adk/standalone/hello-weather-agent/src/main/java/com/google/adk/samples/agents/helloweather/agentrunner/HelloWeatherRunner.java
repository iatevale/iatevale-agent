package com.google.adk.samples.agents.helloweather.agentrunner;

import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.util.function.Consumer;

public class HelloWeatherRunner {

    final private InMemoryRunner runner;
    final private Session session;
    final private RunConfig runConfig;

    public HelloWeatherRunner(InMemoryRunner runner, Session session) {
        this.runner = runner;
        this.session = session;
        this.runConfig = RunConfig.builder().build();
    }

    public boolean execute(String prompt, Consumer<String> consoleOutput) {

        final Content userMsg = Content.fromParts(Part.fromText(prompt));
        final Flowable<Event> events = runner.runAsync(
                session.userId(),
                session.id(),
                userMsg,
                runConfig
        );

        System.out.print("\nAgent > ");
        events.blockingForEach(event -> {
            if (event.finalResponse()) {
                System.out.println(event.stringifyContent());
            }
        });

        return true;
    }

}
