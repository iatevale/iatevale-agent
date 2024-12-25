package org.iatevale.example.vertextai.example08;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

import java.io.IOException;

public class SystemInstruction {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {

            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName("gemino-pro")
                            .setVertexAi(vertexAi)
                            .setSystemInstruction(
                                    ContentMaker.fromString(
                                            "You're a helpful assistant that starts all its answers with: \"COOL\"")
                            )
                            .build();

            GenerateContentResponse response = model.generateContent("How are you?");
            // Do something with the response
        }
    }

}
