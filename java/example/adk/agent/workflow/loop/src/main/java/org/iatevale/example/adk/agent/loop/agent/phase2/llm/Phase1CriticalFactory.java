package org.iatevale.example.adk.agent.loop.agent.phase2.llm;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

import static com.google.adk.agents.LlmAgent.IncludeContents.NONE;

public class Phase1CriticalFactory {

    public static final String STATE_CRITICISM = "criticism";

    static public LlmAgent instantiate() {
        return LlmAgentFactory.child()
                .name("CriticAgent")
                .description(
                        "Reviews the current draft, providing critique if clear improvements are needed,"
                                + " otherwise signals completion.")
                .instruction(
                        """
                                You are a Constructive Critic AI reviewing a short document draft (typically 2-6 sentences). Your goal is balanced feedback.
                                
                                **Document to Review:**
                                ```
                                {{current_document}}
                                ```
                                
                                **Task:**
                                Review the document for clarity, engagement, and basic coherence according to the initial topic (if known).
                                
                                IF you identify 1-2 *clear and actionable* ways the document could be improved to better capture the topic or enhance reader engagement (e.g., "Needs a stronger opening sentence", "Clarify the character's goal"):
                                Provide these specific suggestions concisely. Output *only* the critique text.
                                
                                ELSE IF the document is coherent, addresses the topic adequately for its length, and has no glaring errors or obvious omissions:
                                Respond *exactly* with the phrase "No major issues found." and nothing else. It doesn't need to be perfect, just functionally complete for this stage. Avoid suggesting purely subjective stylistic preferences if the core is sound.
                                
                                Do not add explanations. Output only the critique OR the exact completion phrase.
                                """)
                .outputKey(STATE_CRITICISM)
                .includeContents(NONE)
                .build();
    }

}
