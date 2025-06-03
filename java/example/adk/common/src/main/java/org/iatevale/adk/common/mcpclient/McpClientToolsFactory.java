package org.iatevale.adk.common.mcpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.SseServerParameters;
import org.iatevale.adk.common.logger.AgentLogger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public record McpClientToolsFactory(List<BaseTool> tools) {

    static public McpClientToolsFactory instantiate(McpClientConfig mcpClientConfig) throws McpClientInstanceException {
        return new McpClientToolsFactory(extract(mcpClientConfig.parameters()));
    }

    static public List<BaseTool> extract(SseServerParameters sseServerParameters) throws McpClientInstanceException {

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
            throw new McpClientInstanceException("Se ha producido una interrupcion del thread al extraer las Tools cargadas previamente", e);
        } catch (ExecutionException e) {
            throw new McpClientInstanceException("Se ha producido una error al extraer las Tools cargadas previamente", e);
        }

    }

    static Optional<McpToolset.McpToolsAndToolsetResult> load(SseServerParameters parameters) throws McpClientInstanceException {
        try {
            // La operacion se bloquea hasta que se cargan las herramientas (se pasa de sincrona a asincrona)
            return Optional.ofNullable(
                    McpToolset.fromServer(parameters, new ObjectMapper()).get()
            );
        } catch (InterruptedException e) {
            throw new McpClientInstanceException("Se ha producido una interrupcion del thread al cargar la Tool remota", e);
        } catch (ExecutionException e) {
            throw new McpClientInstanceException("Se ha producido una error de ejecucion al cargar la Tool remota", e);
        }
    }

}
