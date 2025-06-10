package org.iatevale.example.adk.tool.function.agent;

import com.google.adk.agents.LlmAgent;
import com.google.common.collect.ImmutableList;
import org.iatevale.example.adk.common.model.LlmAgentFactory;
import org.iatevale.example.adk.tool.function.tool.CreateTicketToolFactory;

public class RootAgentFactory {

    static public LlmAgent instantiate() {
        return LlmAgentFactory.root()
                .name("ticket_agent")
                .description("Agent for creating tickets via a long-running task.")
                .tools(ImmutableList.of(CreateTicketToolFactory.instantiate()))
                .build();
    }

}
