package org.iatevale.example.vertextai.generativeai.example08;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.iatevale.example.vertextai.common.VertextaiUtil;

import java.io.IOException;

// Construye la instancia de GenerativeModel mediante un builder el cual
// permite proporcioarle instrucciones de sistema.
public class SystemInstruction {

    static final private String MODEL_NAME = "gemini-pro";

    public static void main(String[] args) throws IOException {

        try (VertexAI vertexAi = VertextaiUtil.vertexBuilder().build()) {

            final Content systemInstruction = ContentMaker.fromString(
                    "You're a helpful assistant that starts all its answers with: \"COOL\""
            );

            final GenerativeModel model = new GenerativeModel.Builder()
                    .setModelName(MODEL_NAME)
                    .setVertexAi(vertexAi)
                    .setSystemInstruction(systemInstruction)
                    .build();

            final GenerateContentResponse response = model.generateContent("How are you?");
            System.out.println(ResponseHandler.getText(response));

        }
    }

}
