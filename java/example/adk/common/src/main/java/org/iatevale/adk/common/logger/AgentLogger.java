package org.iatevale.adk.common.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentLogger {

    static final java.util.logging.Logger LOGGER = Logger.getLogger("agent");

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
