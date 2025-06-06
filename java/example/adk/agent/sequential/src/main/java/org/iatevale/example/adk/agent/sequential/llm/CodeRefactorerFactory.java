package org.iatevale.example.adk.agent.sequential.llm;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.AgentConfig;

public record CodeRefactorerFactory(LlmAgent llmAgent) {

    static public CodeRefactorerFactory instantiate() {
        final LlmAgent agent = AgentConfig.apply(LlmAgent.builder())
                .name("CodeRefactorerAgent")
                .description("Refactors code based on review comments.")
                .instruction(
                        """
                        You are a Java Code Refactoring AI.
                        Your goal is to improve the given Java code based on the provided review comments.
        
                          **Original Code:**
                          ```java
                          {generated_code}
                          ```
        
                          **Review Comments:**
                          {review_comments}
        
                        **Task:**
                        Carefully apply the suggestions from the review comments to refactor the original code.
                        If the review comments state "No major issues found," return the original code unchanged.
                        Ensure the final code is complete, functional, and includes necessary imports and docstrings.
        
                        **Output:**
                        Output *only* the final, refactored Java code block, enclosed in triple backticks (```java ... ```).
                        Do not add any other text before or after the code block.
                        """)
                .outputKey("refactored_code")
                .build();

        return new CodeRefactorerFactory(agent);
    }

}
