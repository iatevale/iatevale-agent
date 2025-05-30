package com.google.adk.samples.agents.timeseriesforecasting.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentLogger;
import com.google.adk.samples.agents.timeseriesforecasting.util.AgentServerException;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.SseServerParameters;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class RemoteTools {

    static public RemoteTools instantiate(RemoteConfig remoteConfig) throws AgentServerException {

        // Se carga la lista de herramientas desde el servidor MCP
        final Optional<McpToolset.McpToolsAndToolsetResult> loaded = load(remoteConfig.getParameters());
        if (loaded.isEmpty()) {
            AgentLogger.warning("No se han podido cargar las tools desde el servidor  " + remoteConfig.getParameters().url() + " (load method returned null)");
            return new RemoteTools(Collections.emptyList());
        }

        // Se extraen las tools de la lista cargada previamente (y se adaptan a la interfaz BaseTool que es la universal)
        try (McpToolset toolset = loaded.get().getToolset()) {

            // Se extraen y adapta
            final List<BaseTool> list = toolset.loadTools()
                    .get()
                    .stream()
                    .map(mcpTool -> (BaseTool) mcpTool)
                    .toList();

            // Entregamos el resultado
            return new RemoteTools(list);

        } catch (InterruptedException e) {
            throw new AgentServerException("Se ha producido una interrupcion del thread al extraer las Tools cargadas previamente", e);
        } catch (ExecutionException e) {
            throw new AgentServerException("Se ha producido una error al extraer las Tools cargadas previamente", e);
        }

    }

    static public List<BaseTool> extract(SseServerParameters sseServerParameters) throws AgentServerException {

        try {

            Optional<McpToolset.McpToolsAndToolsetResult> toolsFromServer = load(sseServerParameters);

            if (toolsFromServer.isEmpty()) {
                AgentLogger.warning("Failed to load tools from MCP server at " + sseServerParameters.url() + " (load method returned null)");
                return Collections.emptyList();

            } else {
                try (McpToolset toolset = toolsFromServer.get().getToolset()) {
                    return toolset.loadTools().get().stream().map(mcpTool -> (BaseTool) mcpTool).toList();
                }

            }
        } catch (InterruptedException e) {
            throw new AgentServerException("Se ha producido una interrupcion del thread al extraer las Tools cargadas previamente", e);
        } catch (ExecutionException e) {
            throw new AgentServerException("Se ha producido una error al extraer las Tools cargadas previamente", e);
        }

    }

    static Optional<McpToolset.McpToolsAndToolsetResult> load(SseServerParameters parameters) throws AgentServerException {
        try {
            // La operacion se bloquea hasta que se cargan las herramientas (se pasa de sincrona a asincrona)
            return Optional.ofNullable(
                    McpToolset.fromServer(parameters, new ObjectMapper()).get()
            );
        } catch (InterruptedException e) {
            throw new AgentServerException("Se ha producido una interrupcion del thread al cargar la Tool remota", e);
        } catch (ExecutionException e) {
            throw new AgentServerException("Se ha producido una error de ejecucion al cargar la Tool remota", e);
        }
    }

    // Instancia

    final private List<BaseTool> tools;

    public RemoteTools(List<BaseTool> tools) {
        this.tools = tools;
    }

    public List<BaseTool> getTools() {
        return tools;
    }

}
