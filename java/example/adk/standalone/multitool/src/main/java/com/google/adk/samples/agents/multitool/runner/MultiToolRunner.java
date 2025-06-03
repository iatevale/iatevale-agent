package com.google.adk.samples.agents.multitool.runner;

import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;

import java.util.function.Consumer;

public class MultiToolRunner {

    final private InMemoryRunner runner;
    final private Session session;
    final private RunConfig runConfig;

    public MultiToolRunner(InMemoryRunner runner, Session session) {
        this.runner = runner;
        this.session = session;
        this.runConfig = RunConfig.builder().build();
    }

    public void execute(String prompt, Consumer<String> consoleOutput) {

        final Content userMsg = Content.fromParts(Part.fromText(prompt));
        final Flowable<Event> events = runner.runAsync(
                session.userId(),
                session.id(),
                userMsg,
                runConfig
        );

        consoleOutput.accept("\nAgent > ");
        events.blockingForEach(event -> {
            if (event.finalResponse()) {
                consoleOutput.accept(event.stringifyContent());
            }
        });

    }

}
