package com.google.adk.samples.agents.timeseriesforecasting.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.iatevale.adk.common.logger.AgentLogger;
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
        return new RemoteTools(extract(remoteConfig.getParameters()));
    }

    static public List<BaseTool> extract(SseServerParameters sseServerParameters) throws AgentServerException {

        try {

            final Optional<McpToolset.McpToolsAndToolsetResult> toolsFromServer = load(sseServerParameters);

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
