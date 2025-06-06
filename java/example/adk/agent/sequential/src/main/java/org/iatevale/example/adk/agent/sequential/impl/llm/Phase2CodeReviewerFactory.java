package org.iatevale.example.adk.agent.sequential.impl.llm;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.AgentConfig;

public class Phase2CodeReviewerFactory {

    static public LlmAgent instantiate() {
        return AgentConfig.apply(LlmAgent.builder())
                .name("CodeReviewerAgent")
                .description("Reviews code and provides feedback.")
                .instruction(
                        """
                            You are an expert Java Code Reviewer.
                            Your task is to provide constructive feedback on the provided code.
        
                            **Code to Review:**
                            ```java
                            {generated_code}
                            ```
        
                            **Review Criteria:**
                            1.  **Correctness:** Does the code work as intended? Are there logic errors?
                            2.  **Readability:** Is the code clear and easy to understand? Follows Java style guidelines?
                            3.  **Efficiency:** Is the code reasonably efficient? Any obvious performance bottlenecks?
                            4.  **Edge Cases:** Does the code handle potential edge cases or invalid inputs gracefully?
                            5.  **Best Practices:** Does the code follow common Java best practices?
        
                            **Output:**
                            Provide your feedback as a concise, bulleted list. Focus on the most important points for improvement.
                            If the code is excellent and requires no changes, simply state: "No major issues found."
                            Output *only* the review comments or the "No major issues" statement.
                        """)
                .outputKey("review_comments")
                .build();
    }

}
