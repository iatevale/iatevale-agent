package org.iatevale.example.adk.agent.loop.tool;

import com.google.adk.tools.Annotations;
import com.google.adk.tools.FunctionTool;
import com.google.adk.tools.ToolContext;

import java.util.Map;

public record ExitLoopTool(FunctionTool functionTool) {

    static public ExitLoopTool instantiate() {
        return new ExitLoopTool(
            FunctionTool.create(ExitLoopTool.class, "exitLoop")
        );
    }

    @Annotations.Schema(description = "Call this function ONLY when the critique indicates no further changes are needed, signaling the iterative process should end.")
    public static Map<String, Object> exitLoop(@Annotations.Schema(name = "toolContext") ToolContext toolContext) {
        System.out.printf("[Tool Call] exitLoop triggered by %s \n", toolContext.agentName());
        toolContext.actions().setEscalate(true);
        //  Return empty dict as tools should typically return JSON-serializable output
        return Map.of();
    }

}
