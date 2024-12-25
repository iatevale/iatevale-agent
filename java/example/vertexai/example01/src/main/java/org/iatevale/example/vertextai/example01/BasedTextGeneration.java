package org.iatevale.example.vertextai.example01;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import java.io.IOException;

public class BasedTextGeneration {

    // Reemplaza con tu clave de API de Vertex AI
    private static final String API_KEY = "TU_API_KEY";

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {

            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            GenerateContentResponse response = model.generateContent("How are you?");
            // Do something with the response
        }
    }

}