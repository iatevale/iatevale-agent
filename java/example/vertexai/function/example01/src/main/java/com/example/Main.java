package com.example;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.*;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.protobuf.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        String projectId = "your-gcp-project-id"; // Reemplazar por tu project ID
        String location = "us-central1"; // Reemplazar por la localizacion deseada
        String modelName = "gemini-1.0-pro"; // Reemplazar por el modelo deseado

        try (VertexAI vertexAI = new VertexAI(projectId, location)) {

            // Obtener la instancia de FunctionDefinitions y la herramienta
            FunctionDefinitions functionDefinitions = FunctionDefinitions.getInstance();
            Tool tool = functionDefinitions.getTool();

            // Crear el modelo generativo con la herramienta
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            model = model.toBuilder().setToolsList(Arrays.asList(tool)).build();

            // Ejemplo de conversación con el modelo
            ChatSession chat = model.startChat();

            // Primer mensaje al modelo
            String userMessage = "Puede crear un cliente llamado John Doe con el correo electrónico john.doe@example.com, " +
                    "el número de teléfono 555-4321 y la dirección 321 Pine Ln, Denver, CO, 54321?";
            GenerateContentResponse response = chat.sendMessage(userMessage);
            System.out.println("Prompt:\n" + userMessage);
            printResponse(response);
            response = handleFunctionCall(functionDefinitions, chat, response);

            // Segundo mensaje al modelo
            userMessage = "Puede obtener la información del cliente con el ID 1?";
            response = chat.sendMessage(userMessage);
            System.out.println("Prompt:\n" + userMessage);
            printResponse(response);
            response = handleFunctionCall(functionDefinitions, chat, response);

            // Tercer mensaje al modelo
            userMessage = "Puede crear una orden para el cliente con el ID 1 con los siguientes items: Laptop, Mouse y el precio total es de 1250.99?";
            response = chat.sendMessage(userMessage);
            System.out.println("Prompt:\n" + userMessage);
            printResponse(response);
            handleFunctionCall(functionDefinitions, chat, response);

        }
    }

    /**
     * Maneja la llamada a la función y envía la respuesta al modelo.
     * @param functionDefinitions Instancia de FunctionDefinitions.
     * @param chat Sesión de chat.
     * @param response Respuesta del modelo.
     * @return Respuesta del modelo después de enviar la respuesta de la función.
     */
    private static GenerateContentResponse handleFunctionCall(FunctionDefinitions functionDefinitions, ChatSession chat, GenerateContentResponse response) throws Exception {
        if (ResponseHandler.getFunctionCalls(response).size() > 0) {
            List<Content> contents = new ArrayList<>();

            // Iterar sobre las llamadas a funciones
            for (FunctionCall functionCall : ResponseHandler.getFunctionCalls(response)) {

                // Obtener el nombre de la función y los argumentos
                String functionName = functionCall.getName();
                Map<String, Value> args = functionCall.getArgs().getFieldsMap();

                // Invocar la función y obtener la respuesta
                String functionResponse = functionDefinitions.invokeFunction(functionName, args);

                // Crear la respuesta de la función y añadirla a la lista de contenidos
                Content content = Content.newBuilder()
                        .addParts(Part.newBuilder()
                                .setFunctionResponse(FunctionResponse.newBuilder()
                                        .setName(functionName)
                                        .setResponse(com.google.protobuf.Struct.newBuilder()
                                                .putFields("content", Value.newBuilder().setStringValue(functionResponse).build())
                                                .build())
                                        .build())
                                .build())
                        .build();
                contents.add(content);

                System.out.println("\nFunction Response:\n" + functionResponse);
            }

            // Enviar la respuesta de la función al modelo
            response = chat.sendMessage(contents);
            printResponse(response);
            return response;

        } else {
            return response;
        }
    }

    /**
     * Imprime la respuesta del modelo.
     * @param response Respuesta del modelo.
     */
    private static void printResponse(GenerateContentResponse response) {
        System.out.println("Generative AI response:");
        System.out.println(ResponseHandler.getText(response));
    }

}