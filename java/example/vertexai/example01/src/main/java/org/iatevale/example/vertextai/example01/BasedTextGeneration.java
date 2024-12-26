package org.iatevale.example.vertextai.example01;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Candidate;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.iatevale.example.vertextai.common.VertextaiUtil;
import org.iatevale.util.conversion.ProtoUtils;

import java.io.IOException;
import java.util.List;

public class BasedTextGeneration {

    public static void main(String[] args) throws IOException {

        // Con el VertexAI.Builder generamos una instancia de VertexAI
        // VertexAI es Autocloseable asi que utilizamos esta funcionalidad
        // para que a la salida del contexto se cierre.
        try (VertexAI vertexAi = VertextaiUtil.vertexBuilder().build()) {


            // Esta instancia es en realidad quien interactuara con el modelo
            // y se puede utilizar en entornos multithread.
            // Los objetos generados por el metodo startChat() no son threads-safe
            final GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            // Este metodo genera una respuesta basandose en el cotenido que le proporcionamos
            // GenerativeModel tiene los metodos:
            // - generateContent(Content content): GenerateContentResponse
            // - generateContent(List<Content> contents): GenerateContentResponse
            // - generateContent(String text): GenerateContentResponse
            // El generateContent(String text)  parece una utilidad para invocar
            // a generateContent(Content content).
            //
            // Context es un proto: https://github.com/googleapis/googleapis/blob/master/google/ai/generativelanguage/v1/content.proto
            final GenerateContentResponse response = model.generateContent("How are you?");

            // GenerateContextResponse est tambien otro proto -> https://github.com/googleapis/googleapis/blob/master/google/ai/generativelanguage/v1/generative_service.proto
            // Y es enorme...
            // Asi que en este ejemplo vamos a volcarlo completo para ver que cosas tiene informadas...
            System.out.println(ProtoUtils.messageToJson(response));
//            final List<Candidate> candidates = response.getCandidatesList();
//            VertextaiUtil.displayCandidate(candidates);

        }
    }

}