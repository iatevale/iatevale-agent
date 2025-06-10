package org.iatevale.example.adk.tool.function.googlesearch;

import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.Runner;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import org.iatevale.example.adk.tool.function.googlesearch.agent.RootAgentFactory;

import java.util.Optional;

public class GoogleSearchAgentApp {

    private static final String APP_NAME = "Google Search_agent";
    private static final String USER_ID = "user1234";
    private static final String SESSION_ID = "1234";

    public static void main(String[] args) {
        callAgent("what's the latest ai news?");
    }

    public static void callAgent(String query) {

        final LlmAgent rootAgent = RootAgentFactory.instantiate();
        final InMemorySessionService sessionService = new InMemorySessionService();
        final Runner runner = new Runner(rootAgent, APP_NAME, null, sessionService);

        final Content content = Content.fromParts(Part.fromText(query));

        final Session session = sessionService
                .createSession(APP_NAME, USER_ID, /* state= */ null, SESSION_ID)
                .blockingGet();

        runner
                .runAsync(session.userId(), session.id(), content)
                .forEach(event -> getStringResponse(event)
                        .ifPresent(stringResponse -> System.out.println("Agent response: " + stringResponse))
                );
    }

    static public Optional<String> getStringResponse(Event event) {
        if (event.finalResponse()
                && event.content().isPresent()
                && event.content().get().parts().isPresent()
                && !event.content().get().parts().get().isEmpty()
                && event.content().get().parts().get().get(0).text().isPresent()) {
            return Optional.of(event.content().get().parts().get().get(0).text().get());
        }
        return Optional.empty();
    }

}