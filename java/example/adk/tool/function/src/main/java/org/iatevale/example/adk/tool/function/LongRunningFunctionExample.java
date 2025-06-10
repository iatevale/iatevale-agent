package org.iatevale.example.adk.tool.function;

import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.runner.Runner;
import com.google.adk.sessions.Session;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.genai.types.Content;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import org.iatevale.example.adk.tool.function.agent.RootAgentFactory;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class LongRunningFunctionExample {

    final private static String USER_ID = "user123";

    public static void main(String[] args) {
        final LlmAgent agent = RootAgentFactory.instantiate();
        final Runner runner = new InMemoryRunner(agent);
        final Session session = runner.sessionService().createSession(agent.name(), USER_ID, null, null).blockingGet();
        final LongRunningFunctionExample example = new LongRunningFunctionExample(runner, session);
        example.turn1();
        final String ticketId = example.turn2();
        example.turn3(ticketId);
        System.out.println("Long running function completed successfully.");
    }

    final private Runner runner;
    final private Session session;
    final AtomicReference<String> funcCallIdRef;

    public LongRunningFunctionExample(Runner runner, Session session) {
        this.runner = runner;
        this.session = session;
        this.funcCallIdRef = new AtomicReference<>();
    }

    private void turn1() {

        // --- Turn 1: User requests ticket ---
        System.out.println("\n--- Turn 1: User Request ---");
        Content initialUserMessage = Content.fromParts(Part.fromText("Create a high urgency ticket for me."));

        final Flowable<Event> eventFlowable = runner.runAsync(USER_ID, session.id(), initialUserMessage);
        //eventFlowable.blockingForEach(event -> {});
        eventFlowable.blockingForEach(event -> {
                    //printEventSummary(event, "T1");
                    if (funcCallIdRef.get() == null) { // Capture the first relevant function call ID
                        event.content().flatMap(Content::parts).orElse(ImmutableList.of()).stream()
                                .map(Part::functionCall)
                                .flatMap(Optional::stream)
                                .filter(fc -> "create_ticket_long_running".equals(fc.name().orElse("")))
                                .findFirst()
                                .flatMap(FunctionCall::id)
                                .ifPresent(funcCallIdRef::set);
                    }
                }
        );

        if (funcCallIdRef.get() == null) {
            System.out.println("ERROR: Tool 'create_ticket_long_running' not called in Turn 1.");
            return;
        }
        System.out.println("ACTION: Captured FunctionCall ID: " + funcCallIdRef.get());

    }

    private String turn2() {

        // --- Turn 2: App provides initial ticket_id (simulating async tool completion) ---
        System.out.println("\n--- Turn 2: App provides ticket_id ---");
        String ticketId = "TICKET-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        final FunctionResponse ticketCreatedFuncResponse = FunctionResponse.builder()
                .name("create_ticket_long_running")
                .id(funcCallIdRef.get())
                .response(ImmutableMap.of("ticket_id", ticketId))
                .build();
        final Content appResponseWithTicketId = Content.builder()
                .parts(ImmutableList.of(Part.builder().functionResponse(ticketCreatedFuncResponse).build()))
                .role("user")
                .build();

        runner
                .runAsync(USER_ID, session.id(), appResponseWithTicketId)
                .blockingForEach(event -> printEventSummary(event, "T2"));

        System.out.println("ACTION: Sent ticket_id " + ticketId + " to agent.");

        return ticketId;

    }

    private void turn3(String ticketId) {
        // --- Turn 3: App provides ticket status update ---
        System.out.println("\n--- Turn 3: App provides ticket status ---");
        FunctionResponse ticketStatusFuncResponse =
                FunctionResponse.builder()
                        .name("create_ticket_long_running")
                        .id(funcCallIdRef.get())
                        .response(ImmutableMap.of("status", "approved", "ticket_id", ticketId))
                        .build();
        Content appResponseWithStatus =
                Content.builder()
                        .parts(
                                ImmutableList.of(Part.builder().functionResponse(ticketStatusFuncResponse).build()))
                        .role("user")
                        .build();

        runner
                .runAsync(USER_ID, session.id(), appResponseWithStatus)
                .blockingForEach(event -> printEventSummary(event, "T3_FINAL"));

    }

    private static void printEventSummary(Event event, String turnLabel) {
        event
                .content()
                .ifPresent(content -> {
                        String text = content.parts().orElse(ImmutableList.of()).stream()
                                .map(part -> part.text().orElse(""))
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.joining(" "));
                        if (!text.isEmpty()) {
                            System.out.printf("[%s][%s_TEXT]: %s%n", turnLabel, event.author(), text);
                        }
                        content.parts().orElse(ImmutableList.of()).stream()
                                .map(Part::functionCall)
                                .flatMap(Optional::stream)
                                .findFirst() // Assuming one function call per relevant event for simplicity
                                .ifPresent(fc ->
                                        System.out.printf(
                                                "[%s][%s_CALL]: %s(%s) ID: %s%n",
                                                turnLabel,
                                                event.author(),
                                                fc.name().orElse("N/A"),
                                                fc.args().orElse(ImmutableMap.of()),
                                                fc.id().orElse("N/A")
                                        )
                                );
                        }
                );
    }

}