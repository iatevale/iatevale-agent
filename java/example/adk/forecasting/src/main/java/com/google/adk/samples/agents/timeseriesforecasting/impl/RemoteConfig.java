package com.google.adk.samples.agents.timeseriesforecasting.impl;

import com.google.adk.samples.agents.timeseriesforecasting.util.AgentConfigException;
import com.google.adk.tools.mcp.SseServerParameters;

public class RemoteConfig {

    static public final String TOOLS_SERVER_URL = "http://localhost:8080";

    static public RemoteConfig instantiate() throws AgentConfigException {
        return new RemoteConfig(SseServerParameters.builder()
                .url(checkUrl(TOOLS_SERVER_URL))
                .build());
    }

    static public RemoteConfig instantiate(String serverUrl) throws AgentConfigException {
        return new RemoteConfig(SseServerParameters.builder()
                .url(checkUrl(serverUrl))
                .build());
    }

    static private String checkUrl(String url) throws AgentConfigException {
        if (url == null || url.trim().isEmpty()) {
            throw new AgentConfigException("Tools server URL cannot be null or empty");
        }
        return url;
    }

    // Instancia

    final private SseServerParameters parameters;

    public RemoteConfig(SseServerParameters parameters) {
        this.parameters = parameters;
    }

    public SseServerParameters getParameters() {
        return parameters;
    }

}
