package org.iatevale.example.adk.simple.custom.agent;

import com.google.adk.agents.*;
import com.google.adk.events.Event;
import io.reactivex.rxjava3.core.Flowable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomAgentImpl extends BaseAgent {

    private static final Logger logger = Logger.getLogger(CustomAgentImpl.class.getName());

    final private LlmAgent storyGenerator;
    final private LoopAgent loopAgent;
    final private SequentialAgent sequentialAgent;

    public CustomAgentImpl(String name, LlmAgent storyGenerator, LoopAgent loopAgent, SequentialAgent sequentialAgent) {
        super(
                name,
                "Orchestrates story generation, critique, revision, and checks.",
                List.of(storyGenerator, loopAgent, sequentialAgent),
                null,
                null
        );
        this.storyGenerator = storyGenerator;
        this.loopAgent = loopAgent;
        this.sequentialAgent = sequentialAgent;
    }

    @Override
    protected Flowable<Event> runAsyncImpl(InvocationContext invocationContext) {

        // Implements the custom orchestration logic for the story workflow.
        // Uses the instance attributes assigned by Pydantic (e.g., self.story_generator).
        logger.log(Level.INFO, () -> String.format("[%s] Starting story generation workflow.", name()));

        // Stage 1. Initial Story Generation
        final Flowable<Event> storyGenFlow = runStage(storyGenerator, invocationContext, "StoryGenerator");

        // Stage 2: Critic-Reviser Loop (runs after story generation completes)
        final Flowable<Event> criticReviserFlow = Flowable.defer(() -> {
                    if (!isStoryGenerated(invocationContext)) {
                        logger.log(Level.SEVERE,() -> String.format("[%s] Failed to generate initial story. Aborting after StoryGenerator.", name()));
                        return Flowable.empty(); // Stop further processing if no story
                    }
                    logger.log(Level.INFO, () ->String.format("[%s] Story state after generator: %s", name(), invocationContext.session().state().get("current_story")));
                    return runStage(loopAgent, invocationContext, "CriticReviserLoop");
                }
        );

        // Stage 3: Post-Processing (runs after critic-reviser loop completes)
        final Flowable<Event> postProcessingFlow = Flowable.defer(() -> {
                    logger.log(Level.INFO, () -> String.format("[%s] Story state after loop: %s",
                            name(), invocationContext.session().state().get("current_story"))
                    );
                    return runStage(sequentialAgent, invocationContext, "PostProcessing");
                }
        );

        // Stage 4: Conditional Regeneration (runs after post-processing completes)
        final Flowable<Event> conditionalRegenFlow = Flowable.defer(() -> {
                    final String toneCheckResult = (String) invocationContext.session().state().get("tone_check_result");
                    logger.log(Level.INFO, () -> String.format("[%s] Tone check result: %s", name(), toneCheckResult));

                    if ("negative".equalsIgnoreCase(toneCheckResult)) {
                        logger.log(Level.INFO, () -> String.format("[%s] Tone is negative. Regenerating story...", name()));
                        return runStage(storyGenerator, invocationContext, "StoryGenerator (Regen)");
                    } else {
                        logger.log(Level.INFO, () -> String.format("[%s] Tone is not negative. Keeping current story.", name()));
                        return Flowable.empty(); // No regeneration needed
                    }
                }
        );

        return Flowable.concatArray(storyGenFlow, criticReviserFlow, postProcessingFlow, conditionalRegenFlow)
                .doOnComplete(() -> logger.log(Level.INFO, () -> String.format("[%s] Workflow finished.", name())));
    }

    @Override
    protected Flowable<Event> runLiveImpl(InvocationContext invocationContext) {
        return Flowable.error(new UnsupportedOperationException("runLive not implemented."));
    }

    // Helper method for a single agent run stage with logging
    private Flowable<Event> runStage(BaseAgent agentToRun, InvocationContext ctx, String stageName) {
        logger.log(Level.INFO, () -> String.format("[%s] Running %s...", name(), stageName));
        return agentToRun
                .runAsync(ctx)
                .doOnNext(event -> logger.log(Level.INFO,() -> String.format("[%s] Event from %s: %s", name(), stageName, event.toJson())))
                .doOnError(err -> logger.log(Level.SEVERE, String.format("[%s] Error in %s", name(), stageName), err))
                .doOnComplete(() -> logger.log(Level.INFO, () -> String.format("[%s] %s finished.", name(), stageName)));
    }

    private boolean isStoryGenerated(InvocationContext ctx) {
        final Object currentStoryObj = ctx.session().state().get("current_story");
        return currentStoryObj != null && !String.valueOf(currentStoryObj).isEmpty();
    }

}
