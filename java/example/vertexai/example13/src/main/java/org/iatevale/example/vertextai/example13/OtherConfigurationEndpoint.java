package org.iatevale.example.vertextai.example13;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

import java.io.IOException;

public class OtherConfigurationEndpoint {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";

    private static final String END_POINT = "<new_endpoint>";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI.Builder()
                .setProjectId(PROJECT_ID)
                .setLocation(LOCATION)
                .setApiEndpoint(END_POINT).build()) {

            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            GenerateContentResponse response = model.generateContent("How are you?");
            // Do something with the response

        }

    }

}
