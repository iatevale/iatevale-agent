package org.iatevale.example.vertextai.example02;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseStream;

import java.io.IOException;

public class StreamGeneratedOutput {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";

    public static void main(String[] args) throws IOException {

        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {

            GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream("How are you?");
            // Do something with the ResponseStream, which is an iterable.
        }
    }

}
