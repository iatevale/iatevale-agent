package org.iatevale.adk.common.tool;

import com.google.adk.tools.BaseTool;

public class AbstractToolBuilder {

    final private BaseTool tool;

    protected AbstractToolBuilder(BaseTool tool) {
        this.tool = tool;
    }

    public BaseTool getTool() {
        return tool;
    }

}
