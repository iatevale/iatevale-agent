package org.iatevale.example.adk.tool.function.tool;

import com.google.adk.tools.Annotations;
import com.google.adk.tools.LongRunningFunctionTool;
import com.google.adk.tools.ToolContext;

public class CreateTicketToolFactory {

    static public LongRunningFunctionTool instantiate() {
        return LongRunningFunctionTool.create(CreateTicketToolFactory.class, "createTicketAsync");
    }

    @Annotations.Schema(
        name = "create_ticket_long_running",
        description =
          """
          Creates a new support ticket with a specified urgency level.
          Examples of urgency are 'high', 'medium', or 'low'.
          The ticket creation is a long-running process, and its ID will be provided when ready.
          """
    )
    public static void createTicketAsync(
        @Annotations.Schema(
                name = "urgency",
                description ="The urgency level for the new ticket, such as 'high', 'medium', or 'low'."
        ) String urgency,
        @Annotations.Schema(
                name = "toolContext" // Ensures ADK injection
        ) ToolContext toolContext)
    {
        System.out.printf(
                "TOOL_EXEC: 'create_ticket_long_running' called with urgency: %s (Call ID: %s)%n",
                urgency,
                toolContext.functionCallId().orElse("N/A")
        );
    }

}
