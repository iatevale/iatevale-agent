package com.google.adk.samples.agents.helloweather.impl;

import com.google.adk.tools.Annotations;

import java.util.Map;

public class HelloWeatherTool {

    @Annotations.Schema(description = "Get the weather forecast for a given city")
    public static Map<String, String> getWeather(
            @Annotations.Schema(name = "city", description = "Name of the city to get the weather forecast for") String city) {
        return Map.of(
                "city", city,
                "forecast", "Sunny day, clear blue sky, temperature up to 24°C"
        );
    }

}
