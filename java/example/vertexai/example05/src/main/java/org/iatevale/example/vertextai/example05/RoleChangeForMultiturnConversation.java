package org.iatevale.example.vertextai.example05;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RoleChangeForMultiturnConversation {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";
    private static final String MODEL_NAME = "gemini-pro";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION); ) {
            GenerativeModel model =  new GenerativeModel(MODEL_NAME, vertexAi);

            // Put all the contents in a Content list
            List<Content> contents =
                    Arrays.asList(
                            ContentMaker.fromString("Hi!"),
                            ContentMaker.forRole("model")
                                    .fromString("Hello! How may I assist you?"),
                            ContentMaker.fromString(
                                    "Can you explain quantum mechanis as well in only a few sentences?"));

            // generate the result
            GenerateContentResponse response = model.generateContent(contents);

            // ResponseHandler.getText is a helper function to retrieve the text part of the answer.
            System.out.println("\nPrint response: ");
            System.out.println(ResponseHandler.getText(response));
            System.out.println("\n");
        }
    }

}
