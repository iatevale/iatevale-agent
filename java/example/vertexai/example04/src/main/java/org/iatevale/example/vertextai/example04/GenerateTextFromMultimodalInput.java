package org.iatevale.example.vertextai.example04;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.api.GenerateContentResponse;

public class GenerateTextFromMultimodalInput {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";
    private static final String IMAGE_URI = "<gcs uri to your image>";

    public static void main(String[] args) throws Exception {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION); ) {
            // Vision model must be used for multi-modal input
            GenerativeModel model = new GenerativeModel("gemini-pro-vision", vertexAi);

            ResponseStream<GenerateContentResponse> stream =
                    model.generateContentStream(ContentMaker.fromMultiModalData(
                            "Please describe this image",
                            PartMaker.fromMimeTypeAndData("image/jpeg", IMAGE_URI)
                    ));
            // Do something with the ResponseStream, which is an iterable.
        }
    }

}


