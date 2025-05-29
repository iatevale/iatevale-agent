package com.google.adk.samples.agents.timeseriesforecasting.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.SseServerParameters;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Tools {

    private static final String MCP_TOOLBOX_SERVER_URL_ENV_VAR = "MCP_TOOLBOX_SERVER_URL";

    /**
     * Loads tools from the MCP server.
     *
     * @return The list of tools.
     */
    static public List<BaseTool> getTools() {

        List<BaseTool> tools = ImmutableList.of();

        String mcpServerUrl = System.getenv(MCP_TOOLBOX_SERVER_URL_ENV_VAR);
        AgentLogger.info("MCP Server URL from env: " + mcpServerUrl);

        if (mcpServerUrl == null || mcpServerUrl.trim().isEmpty()) {
            AgentLogger.info(MCP_TOOLBOX_SERVER_URL_ENV_VAR + " environment variable not set. No remote tools will be loaded.");

        } else {
            AgentLogger.info("Attempting to load tools from MCP server: " + mcpServerUrl);

            try {
                SseServerParameters params = SseServerParameters.builder().url(mcpServerUrl).build();
                AgentLogger.fine("URL in SseServerParameters object: " + params.url());

                McpToolset.McpToolsAndToolsetResult toolsAndToolsetResult = McpToolset.fromServer(params, new ObjectMapper()).get();

                if (toolsAndToolsetResult == null) {
                    AgentLogger.warning("Failed to load tools from MCP server at " + mcpServerUrl + ". Load method returned null.");

                } else {
                    
                    McpToolset toolset = (toolsAndToolsetResult != null) ? toolsAndToolsetResult.getToolset() : null;
                    try (McpToolset managedToolset = toolset) {
                        if (toolsAndToolsetResult != null && toolsAndToolsetResult.getTools() != null) {
                            tools = toolsAndToolsetResult.getTools().stream()
                                    .collect(Collectors.toList());
                            AgentLogger.info("Loaded " + tools.size() + " tools.");
                        } else {
                            tools = ImmutableList.of();
                            AgentLogger.warning("Proceeding with an empty tool list due to previous errors or no tools loaded.");
                        }

                        if (tools.isEmpty() && System.getenv(MCP_TOOLBOX_SERVER_URL_ENV_VAR) != null) {
                            AgentLogger.warning(MCP_TOOLBOX_SERVER_URL_ENV_VAR + " was set, but no tools were loaded. Agent will function without these tools.");
                        } else if (tools.isEmpty()) {
                            AgentLogger.warning("No tools are configured for the agent.");
                        }
                    }
                }
            } catch (Exception e) {
                AgentLogger.warning(
                        "Failed to load tools from MCP server at " + mcpServerUrl + ". Ensure the server is running and accessible, and the URL is correct.",
                        e
                );
            }
        }

        return tools;
    }

    static SseServerParameters getServerParameters() throws AgentConfigException {

        final String mcpServerUrl = System.getenv(MCP_TOOLBOX_SERVER_URL_ENV_VAR);

        if (mcpServerUrl == null || mcpServerUrl.trim().isEmpty()) {
            throw new AgentConfigException(MCP_TOOLBOX_SERVER_URL_ENV_VAR + " environment variable not set. No remote tools will be loaded.");
        }

        return SseServerParameters.builder().url(mcpServerUrl).build();

    }

    static Optional<McpToolset.McpToolsAndToolsetResult> getToolsFromServer(SseServerParameters parameters) throws AgentConfigException {
        try {
            return Optional.ofNullable(
                    McpToolset.fromServer(parameters, new ObjectMapper()).get()
            );
        } catch (InterruptedException e) {
            throw new AgentConfigException("Se ha producido una interrupcion del thread al cargar la Tool remota", e);
        } catch (ExecutionException e) {
            throw new AgentConfigException("Se ha producido una error de ejecucion al cargar la Tool remota", e);
        }
    }

}
