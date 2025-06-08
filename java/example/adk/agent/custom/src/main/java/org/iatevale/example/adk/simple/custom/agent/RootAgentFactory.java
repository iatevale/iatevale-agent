package org.iatevale.example.adk.simple.custom.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.LoopAgent;
import com.google.adk.agents.SequentialAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;
import org.iatevale.example.adk.simple.custom.StoryFlowAgentExample;

public class RootAgentFactory {

    private static final String APP_NAME = "story_app";

    public static CustomAgentImpl instantiate() {

        // --- Define the individual LLM agents ---
        LlmAgent storyGenerator = LlmAgentFactory.root()
                .name("StoryGenerator")
                .description("Generates the initial story.")
                .instruction(
                        """
                      You are a story writer. Write a short story (around 100 words) about a cat,
                      based on the topic provided in session state with key 'topic'
                      """)
                .inputSchema(null)
                .outputKey("current_story") // Key for storing output in session state
                .build();

        LlmAgent critic = LlmAgentFactory.root()
                .name("Critic")
                .description("Critiques the story.")
                .instruction(
                        """
                      You are a story critic. Review the story provided in
                      session state with key 'current_story'. Provide 1-2 sentences of constructive criticism
                      on how to improve it. Focus on plot or character.
                      """)
                .inputSchema(null)
                .outputKey("criticism") // Key for storing criticism in session state
                .build();

        LlmAgent reviser = LlmAgentFactory.root()
                .name("Reviser")
                .description("Revises the story based on criticism.")
                .instruction(
                        """
                      You are a story reviser. Revise the story provided in
                      session state with key 'current_story', based on the criticism in
                      session state with key 'criticism'. Output only the revised story.
                      """)
                .inputSchema(null)
                .outputKey("current_story") // Overwrites the original story
                .build();

        LlmAgent grammarCheck = LlmAgentFactory.root()
                .name("GrammarCheck")
                .description("Checks grammar and suggests corrections.")
                .instruction(
                        """
                       You are a grammar checker. Check the grammar of the story
                       provided in session state with key 'current_story'. Output only the suggested
                       corrections as a list, or output 'Grammar is good!' if there are no errors.
                       """)
                .outputKey("grammar_suggestions")
                .build();

        LlmAgent toneCheck = LlmAgentFactory.root()
                .name("ToneCheck")
                .description("Analyzes the tone of the story.")
                .instruction(
                        """
                      You are a tone analyzer. Analyze the tone of the story
                      provided in session state with key 'current_story'. Output only one word: 'positive' if
                      the tone is generally positive, 'negative' if the tone is generally negative, or 'neutral'
                      otherwise.
                      """)
                .outputKey("tone_check_result") // This agent's output determines the conditional flow
                .build();

        LoopAgent loopAgent = LoopAgent.builder()
                .name("CriticReviserLoop")
                .description("Iteratively critiques and revises the story.")
                .subAgents(critic, reviser)
                .maxIterations(2)
                .build();

        SequentialAgent sequentialAgent = SequentialAgent.builder()
                .name("PostProcessing")
                .description("Performs grammar and tone checks sequentially.")
                .subAgents(grammarCheck, toneCheck)
                .build();


        StoryFlowAgentExample storyFlowAgentExample = new StoryFlowAgentExample(
                APP_NAME,
                storyGenerator,
                loopAgent,
                sequentialAgent
        );

        return new CustomAgentImpl()
        // --- Run the Agent ---
        runAgent(storyFlowAgentExample, "a lonely robot finding a friend in a junkyard");
    }
}
