package com.google.adk.samples.agents.sequential.llm;

import com.google.adk.agents.LlmAgent;
import org.iatevale.adk.common.model.AgentConfig;

public record CodeWriterFactory(LlmAgent llmAgent) {

    static public CodeWriterFactory instantiate() {

        final LlmAgent agent = AgentConfig.apply(LlmAgent.builder())
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

        return new CodeWriterFactory(agent);
    }

}
