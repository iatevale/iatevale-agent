package com.google.adk.samples.agents.timeseriesforecasting.impl;

public class AgentConfigException extends Exception {

    public AgentConfigException(String message) {
        super(message);
    }

    public AgentConfigException(String message, Exception ex) {
        super(message, ex);
    }

}
