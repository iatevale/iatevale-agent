package com.google.adk.samples.agents.timeseriesforecasting.impl;

public class AgentInitException extends Exception {

    public AgentInitException(String message) {
        super(message);
    }

    public AgentInitException(String message, Exception ex) {
        super(message, ex);
    }

}
