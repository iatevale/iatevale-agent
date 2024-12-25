package org.iatevale.example.vertextai.example01;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Candidate;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.iatevale.example.vertextai.common.VertextaiUtil;

import java.io.IOException;
import java.util.List;

public class BasedTextGeneration {

    public static void main(String[] args) throws IOException {

        try (VertexAI vertexAi = VertextaiUtil.vertexBuilder().build()) {

            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            GenerateContentResponse response = model.generateContent("How are you?");

            // Obtener y mostrar los candidatos de contenido generados
            final List<Candidate> candidates = response.getCandidatesList();
            VertextaiUtil.displayCandidate(candidates);

        }
    }

}