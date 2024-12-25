package org.iatevale.example.vertextai.example10;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

import java.io.IOException;

public class ConfigurationsForChatSession {

    private static final String PROJECT_ID = "<PROJECT_ID>";
    private static final String LOCATION = "<LOCATION>";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {
            // Instantiate a model with GenerationConfig
            GenerationConfig generationConfig =
                    GenerationConfig.newBuilder().setMaxOutputTokens(50).build();
            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName("gemino-pro")
                            .setVertexAi(vertexAi)
                            .setGenerationConfig(generationConfig)
                            .build();

            // Start a chat session
            ChatSession chat = model.startChat();

            // Send a message. The model level GenerationConfig will be applied here
            GenerateContentResponse response = chat.sendMessage("Please explain LLM?");

            // Do something with the response

            // Send another message, using Fluent API to update the GenerationConfig
            response =
                    chat.withGenerationConfig(GenerationConfig.getDefaultInstance())
                            .sendMessage("Tell me more about what you can do.");

            // Do something with the response
        }
    }

}
