package org.iatevale.example.vertextai.example01;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.iatevale.util.auth.GCloudAuthFactory;
import org.iatevale.util.auth.GCloudAuthParameters;

import java.io.IOException;

public class BasedTextGeneration {

    public static void main(String[] args) throws IOException {

        final GCloudAuthParameters parameters = GCloudAuthFactory.getCredentials();

        final VertexAI.Builder vertextAIBuilder = new VertexAI.Builder()
                .setProjectId(parameters.projectId())
                .setCredentials(parameters.credentials());

        try (VertexAI vertexAi = vertextAIBuilder.build()) {

            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            GenerateContentResponse response = model.generateContent("How are you?");
            // Do something with the response
        }
    }

}