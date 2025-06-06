package com.google.adk.samples.agents.loop.llm;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.BaseTool;
import org.iatevale.adk.common.model.AgentConfig;

import static com.google.adk.agents.LlmAgent.IncludeContents.NONE;

public record RefinerAgentFactory(LlmAgent llmAgent) {

    private static final String STATE_CURRENT_DOC = "current_document";

    static public RefinerAgentFactory instantiate(BaseTool baseTool) {

        final LlmAgent agent = AgentConfig.apply(LlmAgent.builder())
                .name("RefinerAgent")
                .description(
                        "Refines the document based on critique, or calls exitLoop if critique indicates"
                                + " completion.")
                .instruction(
                        """
                                    You are a Creative Writing Assistant refining a document based on feedback OR exiting the process.
                                    **Current Document:**
                                    ```
                                    {{current_document}}
                                    ```
                                    **Critique/Suggestions:**
                                    {{criticism}}
                                
                                    **Task:**
                                    Analyze the 'Critique/Suggestions'.
                                    IF the critique is *exactly* "No major issues found.":
                                    You MUST call the 'exitLoop' function. Do not output any text.
                                    ELSE (the critique contains actionable feedback):
                                    Carefully apply the suggestions to improve the 'Current Document'. Output *only* the refined document text.
                                
                                    Do not add explanations. Either output the refined document OR call the exitLoop function.
                                """)
                .outputKey(STATE_CURRENT_DOC)
                .includeContents(NONE)
                .tools(baseTool)
                .build();

        return new RefinerAgentFactory(agent);

    }

}
