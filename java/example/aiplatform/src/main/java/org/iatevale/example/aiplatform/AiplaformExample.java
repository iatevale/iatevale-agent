package org.iatevale.example.aiplatform;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AiplaformExample {

    // Reemplaza con tu clave de API de Vertex AI
    private static final String API_KEY = "TU_API_KEY";

    public static void main(String[] args) throws IOException, InterruptedException {
        String endpoint = "https://us-central1-aiplatform.googleapis.com/v1/projects/your-project-id/locations/us-central1/publishers/google/models/gemini-pro:predict";

        // Reemplaza "your-project-id" con el ID de tu proyecto
        endpoint = endpoint.replace("your-project-id", "your-project-id");

        HttpClient client = HttpClient.newHttpClient();

        // Crea la solicitud JSON para Gemini
        JSONObject requestBody = new JSONObject();
        requestBody.put("instances", new JSONObject[] {
                new JSONObject().put("content", "¿Cuál es la predicción del tiempo para Morella?")
        });
        requestBody.put("parameters", new JSONObject().put("temperature", 0.2));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Procesa la respuesta de Gemini
        JSONObject jsonResponse = new JSONObject(response.body());
        String prediction = jsonResponse.getJSONArray("predictions").getJSONObject(0).getString("content");

        System.out.println("Predicción del tiempo para Morella:");
        System.out.println(prediction);
    }
}