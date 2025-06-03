package org.iatevale.adk.common.mcpclient;

import com.google.adk.tools.mcp.SseServerParameters;

public record McpClientConfig(SseServerParameters parameters) {

    static public final String TOOLS_SERVER_URL = "http://localhost:8080";

    static public McpClientConfig instantiate() throws McpClientConfigException {
        return new McpClientConfig(SseServerParameters.builder()
                .url(checkUrl(TOOLS_SERVER_URL))
                .build());
    }

    static public McpClientConfig instantiate(String serverUrl) throws McpClientConfigException {
        return new McpClientConfig(SseServerParameters.builder()
                .url(checkUrl(serverUrl))
                .build());
    }

    static private String checkUrl(String url) throws McpClientConfigException {
        if (url == null || url.trim().isEmpty()) {
            throw new McpClientConfigException("Tools server URL cannot be null or empty");
        }
        return url;
    }

}
