package com.google.adk.samples.agents.softwarebugassistant;

import com.google.adk.samples.agents.softwarebugassistant.agent.AgentMainFactory;
import com.google.adk.samples.agents.softwarebugassistant.agent.AgentSearchFactory;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.BaseTool;
import org.iatevale.example.adk.common.mcpclient.McpClientConfig;
import org.iatevale.example.adk.common.mcpclient.McpClientException;
import org.iatevale.example.adk.common.mcpclient.McpClientToolsFactory;

import java.util.List;

public class SoftwareBugAssistant {

    public static void main(String[] args) throws McpClientException {

        // Set up MCP Toolbox connection to Cloud SQL
        final McpClientConfig mcpClientConfig = McpClientConfig.instantiate();
        final McpClientToolsFactory mcpClientToolsFactory = McpClientToolsFactory.instantiate(mcpClientConfig);

        // Add GoogleSearch tool - Workaround for https://github.com/google/adk-python/issues/134
        final AgentSearchFactory agentSearchFactory = AgentSearchFactory.instantiate();

        // Se unen todas las tools
        final List<BaseTool> allTools = mcpClientToolsFactory.tools();
        allTools.add(AgentTool.create(agentSearchFactory.llmAgent()));

        // Main Agent
        final AgentMainFactory agentMainFactory = AgentMainFactory.instantiate(allTools);

    }
}