package org.iatevale.example.vertextai.example06;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import java.io.IOException;
import java.util.List;

public class UseChatSessionForMultiturnChat {

    private static final String PROJECT_ID = "<your project id>";
    private static final String LOCATION = "<location>";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION); ) {
            GenerativeModel model =
                    new GenerativeModel("gemini-pro", vertexAi);
            ChatSession chat = model.startChat();

            // Send the first message.
            // ChatSession also has two versions of sendMessage, stream and non-stream
            ResponseStream<GenerateContentResponse> response = chat.sendMessageStream("Hi!");

            // Do something with the output stream, possibly with ResponseHandler

            // Now send another message. The history will be remembered by the ChatSession.
            // Note: the stream needs to be consumed before you send another message
            // or fetch the history.
            ResponseStream<GenerateContentResponse> anotherResponse = chat.sendMessageStream("Can you explain quantum mechanis as well in a few sentences?");

            // Do something with the second response

            // See the whole history. Make sure you have consumed the stream.
            List<Content> history = chat.getHistory();
        }
    }

}
