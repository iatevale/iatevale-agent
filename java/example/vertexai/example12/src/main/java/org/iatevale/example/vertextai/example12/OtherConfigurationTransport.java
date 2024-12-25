package org.iatevale.example.vertextai.example12;

import java.io.IOException;

public class OtherConfigurationTransport {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";

    public static void main(String[] args) throws IOException {
        // No compila.
        // No encuentra com.google.cloud.vertexai.generativeai.Transport
//        try (VertexAI vertexAi = new VertexAI.Builder()
//                .setProjectId(PROJECT_ID)
//                .setLocation(LOCATION)
//                .setTransport(Transport.REST);) {
//
//            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);
//
//            GenerateContentResponse response = model.generateContent("How are you?");
//            // Do something with the response
//        }
    }

}
