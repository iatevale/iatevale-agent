package org.iatevale.example.adk.tool.function.googlesearch.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;
import com.google.common.collect.ImmutableList;
import org.iatevale.example.adk.common.model.LlmAgentFactory;

public class RootAgentFactory {

    static public LlmAgent instantiate() {
        final GoogleSearchTool googleSearchTool = new GoogleSearchTool();
        return LlmAgentFactory.root()
                .name("basic_search_agent")
                .description("Agent to answer questions using Google Search.")
                .instruction("I can answer your questions by searching the internet. Just ask me anything!")
                .tools(ImmutableList.of(googleSearchTool))
                .build();
    }

}
