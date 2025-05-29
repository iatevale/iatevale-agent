package com.google.adk.samples.agents.timeseriesforecasting.impl;

import com.google.adk.samples.agents.timeseriesforecasting.ForecastingAgent;

import java.util.logging.Level;

public class AgentLogger {

    static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ForecastingAgent.class.getName());

    static public void setLevel(Level level) {
        LOGGER.setLevel(level);
    }

    static public void fine(String msg) {
        LOGGER.fine(msg);
    }

    static public void info(String msg) {
        LOGGER.info(msg);
    }

    static public void warning(String msg) {
        LOGGER.warning(msg);
    }

    static public void warning(String msg, Exception ex) {
        LOGGER.log(Level.WARNING, msg, ex);
    }

}
