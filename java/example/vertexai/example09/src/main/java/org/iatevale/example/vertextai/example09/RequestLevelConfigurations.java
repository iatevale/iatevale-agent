package org.iatevale.example.vertextai.example09;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.api.SafetySetting.HarmBlockThreshold;

import java.io.IOException;

public class RequestLevelConfigurations {

    private static final String PROJECT_ID = "<PROJECT_ID>";
    private static final String LOCATION = "<LOCATION>";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION); ) {
            // Build a SafetySetting instance.
            SafetySetting safetySetting =
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(HarmBlockThreshold.BLOCK_LOW_AND_ABOVE)
                            .build();


            // Generate the response with the fluent API `withSafetySetting`.
            // TODO: Las tres siguientes lineas no compilan por que el ejemplo
            // delrepositorio esta mal -> https://github.com/googleapis/google-cloud-java/tree/main/java-vertexai#add-dependency
//            GenerateContentResponse response = model
//                            .withSafetySetting(Arrays.asList(SafetySetting))
//                            .generateContent("Please explain LLM?");

            // Do something with the response.
        }
    }

}
