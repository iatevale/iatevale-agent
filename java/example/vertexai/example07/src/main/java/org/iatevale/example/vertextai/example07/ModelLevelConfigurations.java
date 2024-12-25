package org.iatevale.example.vertextai.example07;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import java.io.IOException;

public class ModelLevelConfigurations {

    private static final String PROJECT_ID = "<PROJECT_ID>";
    private static final String LOCATION = "<LOCATION>";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {
            // Build a GenerationConfig instance.
            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder().setMaxOutputTokens(50).build();

            // Use the builder to instantialize the model with the configuration.
            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName("gemino-pro")
                            .setVertexAi(vertexAi)
                            .setGenerationConfig(generationConfig)
                            .build();

            // Generate the response.
            GenerateContentResponse response = model.generateContent("Please explain LLM?");

            // Do something with the response.
        }
    }

}
