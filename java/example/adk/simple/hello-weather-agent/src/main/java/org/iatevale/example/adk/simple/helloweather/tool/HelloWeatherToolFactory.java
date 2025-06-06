package org.iatevale.example.adk.simple.helloweather.tool;

import com.google.adk.tools.Annotations;
import com.google.adk.tools.FunctionTool;

import java.util.Map;

public record HelloWeatherToolFactory(FunctionTool functionTool) {

    static public HelloWeatherToolFactory instantiate() {
        FunctionTool functionTool = FunctionTool.create(HelloWeatherToolImpl.class, "getWeather");
        return new HelloWeatherToolFactory(functionTool);
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

}
