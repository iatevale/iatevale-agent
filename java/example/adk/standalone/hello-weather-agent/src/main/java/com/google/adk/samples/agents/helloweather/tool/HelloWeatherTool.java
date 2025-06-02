package com.google.adk.samples.agents.helloweather.tool;

import com.google.adk.tools.Annotations;
import com.google.adk.tools.BaseTool;
import com.google.adk.tools.FunctionTool;

import java.util.Map;

public class HelloWeatherTool {

    static public HelloWeatherTool instantiate() {
        FunctionTool functionTool = FunctionTool.create(HelloWeatherToolImpl.class, "getWeather");
        return new HelloWeatherTool(functionTool);
    }

    static private class HelloWeatherToolImpl {

        @Annotations.Schema(description = "Get the weather forecast for a given city")
        public static Map<String, String> getWeather(
                @Annotations.Schema(name = "city", description = "Name of the city to get the weather forecast for") String city) {
            return Map.of(
                    "city", city,
                    "forecast", "Sunny day, clear blue sky, temperature up to 24°C"
            );
        }

    }

    // Instancia

    final private BaseTool tool;

    public HelloWeatherTool(BaseTool tool) {
        this.tool = tool;
    }

    public BaseTool getTool() {
        return tool;
    }

}
