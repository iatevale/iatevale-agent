package org.iatevale.example.vertextai.example03;

import com.google.api.core.ApiFuture;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TextGenerationWithAsync {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {

            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            ApiFuture<GenerateContentResponse> future = model.generateContentAsync("How are you?");

            // Do something else.

            // Get the response from Future
            GenerateContentResponse response = future.get();

            // Do something with the response.
        }
    }

}
