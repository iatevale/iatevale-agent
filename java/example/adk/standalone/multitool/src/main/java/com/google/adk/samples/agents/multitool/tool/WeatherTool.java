package com.google.adk.samples.agents.multitool.tool;

import com.google.adk.tools.Annotations;

import java.util.Map;

public class WeatherTool {

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
