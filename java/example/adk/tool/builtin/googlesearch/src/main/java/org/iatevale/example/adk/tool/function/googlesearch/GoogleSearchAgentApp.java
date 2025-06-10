package org.iatevale.example.adk.tool.function.googlesearch;

import com.google.adk.agents.BaseAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.Runner;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.sessions.Session;
import com.google.adk.tools.GoogleSearchTool;
import com.google.common.collect.ImmutableList;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

import java.util.Optional;

public class GoogleSearchAgentApp {

    private static final String APP_NAME = "Google Search_agent";
    private static final String USER_ID = "user1234";
    private static final String SESSION_ID = "1234";

    /**
     * Calls the agent with the given query and prints the final response.
     *
     * @param runner The runner to use.
     * @param query The query to send to the agent.
     */
    public static void callAgent(Runner runner, String query) {

        final Content content = Content.fromParts(Part.fromText(query));

        final InMemorySessionService sessionService = (InMemorySessionService) runner.sessionService();
        final Session session = sessionService
                .createSession(APP_NAME, USER_ID, /* state= */ null, SESSION_ID)
                .blockingGet();

        runner
                .runAsync(session.userId(), session.id(), content)
                .forEach(
                    event -> getStringResponse(event)
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

    public static void main(String[] args) {

        final GoogleSearchTool googleSearchTool = new GoogleSearchTool();

        final BaseAgent rootAgent = LlmAgentFactory.root()
                .name("basic_search_agent")
                .model("gemini-2.0-flash") // Ensure to use a Gemini 2.0 model for Google Search Tool
                .description("Agent to answer questions using Google Search.")
                .instruction("I can answer your questions by searching the internet. Just ask me anything!")
                .tools(ImmutableList.of(googleSearchTool))
                .build();

        // Session and Runner
        final InMemorySessionService sessionService = new InMemorySessionService();
        final Runner runner = new Runner(rootAgent, APP_NAME, null, sessionService);

        // Agent Interaction
        callAgent(runner, "what's the latest ai news?");
    }
    
}