package com.google.adk.samples.agents.multitool.tool;

import com.google.adk.tools.Annotations;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.FunctionTool;
import org.iatevale.adk.common.tool.AbstractToolBuilder;

import java.text.Normalizer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CurrentTimeTool extends AbstractToolBuilder {

    static public CurrentTimeTool instantiate() {
        FunctionTool functionTool = FunctionTool.create(CurrentTimeToolImpl.class, "getCurrentTime");
        return new CurrentTimeTool(functionTool);
    }

    static private class CurrentTimeToolImpl {

        public static Map<String, String> getCurrentTime(
                @Annotations.Schema(description = "The name of the city for which to retrieve the current time")
                String city) {
            String normalizedCity =
                    Normalizer.normalize(city, Normalizer.Form.NFD)
                            .trim()
                            .toLowerCase()
                            .replaceAll("(\\p{IsM}+|\\p{IsP}+)", "")
                            .replaceAll("\\s+", "_");

            return ZoneId.getAvailableZoneIds().stream()
                    .filter(zid -> zid.toLowerCase().endsWith("/" + normalizedCity))
                    .findFirst()
                    .map(
                            zid ->
                                    Map.of(
                                            "status",
                                            "success",
                                            "report",
                                            "The current time in "
                                                    + city
                                                    + " is "
                                                    + ZonedDateTime.now(ZoneId.of(zid))
                                                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                                                    + "."))
                    .orElse(
                            Map.of(
                                    "status",
                                    "error",
                                    "report",
                                    "Sorry, I don't have timezone information for " + city + "."));
        }

    }

    public CurrentTimeTool(BaseTool tool) {
        super(tool);
    }

}
