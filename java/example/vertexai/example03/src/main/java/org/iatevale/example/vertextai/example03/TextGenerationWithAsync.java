package org.iatevale.example.vertextai.example03;

import com.google.api.core.ApiFuture;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.iatevale.example.vertextai.common.VertextaiUtil;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class TextGenerationWithAsync {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        try (VertexAI vertexAi = VertextaiUtil.vertexBuilder().build()) {

            final GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            final ApiFuture<GenerateContentResponse> future = model.generateContentAsync("How are you?");

            // Do something else.

            // Get the response from Future
            GenerateContentResponse response = future.get();

            // Do something with the response.
        }
    }

}
