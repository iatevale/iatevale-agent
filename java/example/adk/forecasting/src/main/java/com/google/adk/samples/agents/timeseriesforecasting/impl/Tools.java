package com.google.adk.samples.agents.timeseriesforecasting.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.SseServerParameters;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class Tools {

    private static final String MCP_TOOLBOX_SERVER_URL_ENV_VAR = "MCP_TOOLBOX_SERVER_URL";

    static public List<BaseTool> getTools() throws AgentInitException {
        return getTools(getServerParameters());
    }

    static public List<BaseTool> getTools(SseServerParameters params) throws AgentInitException {
        try {

            Optional<McpToolset.McpToolsAndToolsetResult> toolsFromServer = loadFromServer(params);

            if (toolsFromServer.isEmpty()) {
                AgentLogger.warning("Failed to load tools from MCP server at " + params.url() + " (load method returned null)");
                return Collections.emptyList();

            } else {

                try (McpToolset toolset = toolsFromServer.get().getToolset()) {
                    return toolset.loadTools().get().stream().map(mcpTool -> (BaseTool) mcpTool).toList();
                }
            }
        } catch (InterruptedException e) {
            throw new AgentInitException("Se ha producido una interrupcion del thread al extraer las Tools cargadas previamente", e);
        } catch (ExecutionException e) {
            throw new AgentInitException("Se ha producido una error al extraer las Tools cargadas previamente", e);
        }

    }

    static SseServerParameters getServerParameters() throws AgentInitException {

        final String mcpServerUrl = System.getenv(MCP_TOOLBOX_SERVER_URL_ENV_VAR);

        if (mcpServerUrl == null || mcpServerUrl.trim().isEmpty()) {
            throw new AgentInitException(MCP_TOOLBOX_SERVER_URL_ENV_VAR + " environment variable not set. No remote tools will be loaded.");
        }

        return SseServerParameters.builder().url(mcpServerUrl).build();

    }

    static Optional<McpToolset.McpToolsAndToolsetResult> loadFromServer(SseServerParameters parameters) throws AgentInitException {
        try {
            return Optional.ofNullable(
                    McpToolset.fromServer(parameters, new ObjectMapper()).get()
            );
        } catch (InterruptedException e) {
            throw new AgentInitException("Se ha producido una interrupcion del thread al cargar la Tool remota", e);
        } catch (ExecutionException e) {
            throw new AgentInitException("Se ha producido una error de ejecucion al cargar la Tool remota", e);
        }
    }

}
