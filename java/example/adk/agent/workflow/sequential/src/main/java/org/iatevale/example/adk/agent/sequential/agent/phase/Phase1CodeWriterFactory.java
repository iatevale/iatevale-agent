package org.iatevale.example.adk.agent.sequential.agent.phase;

import com.google.adk.agents.LlmAgent;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class Phase1CodeWriterFactory {

    static public LlmAgent instantiate() {
        return LlmAgentFactory.child()
                .name("CodeWriterAgent")
                .description("Writes initial Java code based on a specification.")
                .instruction(
                        """
                        You are a Java Code Generator.
                        Based *only* on the user's request, write Java code that fulfills the requirement.
                        Output *only* the complete Java code block, enclosed in triple backticks (```java ... ```).
                        Do not add any other text before or after the code block.
                        """)
                .outputKey("generated_code")
                .build();
    }

}
