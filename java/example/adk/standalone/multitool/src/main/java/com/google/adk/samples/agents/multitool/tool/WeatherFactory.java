package com.google.adk.samples.agents.multitool.tool;

import com.google.adk.tools.Annotations;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.FunctionTool;
import org.iatevale.adk.common.tool.AbstractToolBuilder;

import java.util.Map;

public class WeatherFactory extends AbstractToolBuilder {

    static public WeatherFactory instantiate() {
        FunctionTool functionTool = FunctionTool.create(WeatherToolImpl.class, "getWeather");
        return new WeatherFactory(functionTool);
    }

    static private class WeatherToolImpl {

        public static Map<String, String> getWeather(
                @Annotations.Schema(description = "The name of the city for which to retrieve the weather report")
                String city) {
            if (city.equalsIgnoreCase("new york")) {
                return Map.of(
                        "status",
                        "success",
                        "report",
                        "The weather in New York is sunny with a temperature of 25 degrees Celsius (77 degrees Fahrenheit).");

            } else {
                return Map.of(
                        "status", "error", "report", "Weather information for " + city + " is not available.");
            }
        }

    }

    public WeatherFactory(BaseTool tool) {
        super(tool);
    }

}
