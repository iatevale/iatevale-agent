package org.iatevale.adk.common.mcpclient;

public class McpClientException extends Exception {

    public McpClientException(String message) {
        super(message);
    }

    public McpClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
