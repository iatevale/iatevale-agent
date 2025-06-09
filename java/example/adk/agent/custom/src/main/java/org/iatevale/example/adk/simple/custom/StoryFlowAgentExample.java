package org.iatevale.example.adk.simple.custom;

import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import org.iatevale.example.adk.simple.custom.agent.CustomAgentImpl;
import org.iatevale.example.adk.simple.custom.agent.RootAgentFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoryFlowAgentExample {

    // --- Constants ---
    private static final String APP_NAME = "story_app";
    private static final String USER_ID = "user_12345";
    private static final String SESSION_ID = "session_123344";

    private static final Logger logger = Logger.getLogger(StoryFlowAgentExample.class.getName());

    public static void main(String[] args) {
        final StoryFlowAgentExample storyFlowAgentExample1 = new StoryFlowAgentExample();
        runAgent("a lonely robot finding a friend in a junkyard");
    }


    // --- Function to Interact with the Agent ---
    // Sends a new topic to the agent (overwriting the initial one if needed)
    // and runs the workflow.
    public static void runAgent(String userTopic) {

        final CustomAgentImpl customAgent = RootAgentFactory.instantiate();

        InMemoryRunner runner = new InMemoryRunner(customAgent);

        Map<String, Object> initialState = new HashMap<>();
        initialState.put("topic", "a brave kitten exploring a haunted house");

        Session session = runner
                .sessionService()
                .createSession(APP_NAME, USER_ID, new ConcurrentHashMap<>(initialState), SESSION_ID)
                .blockingGet();
        logger.log(Level.INFO, () -> String.format("Initial session state: %s", session.state()));

        session.state().put("topic", userTopic); // Update the state in the retrieved session
        logger.log(Level.INFO, () -> String.format("Updated session state topic to: %s", userTopic));

        Content userMessage = Content.fromParts(Part.fromText("Generate a story about: " + userTopic));
        // Use the modified session object for the run
        Flowable<Event> eventStream = runner.runAsync(USER_ID, session.id(), userMessage);

        final String[] finalResponse = {"No final response captured."};
        eventStream.blockingForEach(event -> {
                    if (event.finalResponse() && event.content().isPresent()) {
                        String author = event.author() != null ? event.author() : "UNKNOWN_AUTHOR";
                        Optional<String> textOpt = event
                                .content()
                                .flatMap(Content::parts)
                                .filter(parts -> !parts.isEmpty())
                                .map(parts -> parts.get(0).text().orElse(""));

                        logger.log(Level.INFO, () ->
                                String.format("Potential final response from [%s]: %s", author, textOpt.orElse("N/A")));
                        textOpt.ifPresent(text -> finalResponse[0] = text);
                    }
                }
        );

        System.out.println("\n--- Agent Interaction Result ---");
        System.out.println("Agent Final Response: " + finalResponse[0]);

        // Retrieve session again to see the final state after the run
        Session finalSession = runner
                .sessionService()
                .getSession(APP_NAME, USER_ID, SESSION_ID, Optional.empty())
                .blockingGet();

        assert finalSession != null;
        System.out.println("Final Session State:" + finalSession.state());
        System.out.println("-------------------------------\n");
    }


}