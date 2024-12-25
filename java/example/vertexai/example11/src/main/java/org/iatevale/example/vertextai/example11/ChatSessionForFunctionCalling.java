package org.iatevale.example.vertextai.example11;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Tool;
import com.google.cloud.vertexai.generativeai.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class ChatSessionForFunctionCalling {

    private static final String PROJECT_ID = "<your project>";
    private static final String LOCATION = "<location>";
    private static final String MODEL_NAME = "gemini-pro";
    private static final String TEXT = "What's the weather in Vancouver?";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION); ) {
            // Declare a function to be used in a request.
            // We construct a jsonString that corresponds to the following function
            // declaration.
            // {
            //   "name": "getCurrentWeather",
            //   "description": "Get the current weather in a given location",
            //   "parameters": {
            //     "type": "OBJECT",
            //     "properties": {
            //       "location": {
            //         "type": "STRING",
            //         "description": "location"
            //       }
            //     }
            //   }
            // }
            // With JDK 15 and above, you can do
            //
            // String jsonString = """
            //   {
            //     "name": "getCurrentWeather",
            //     "description": "Get the current weather in a given location",
            //     "parameters": {
            //       "type": "OBJECT",
            //       "properties": {
            //         "location": {
            //           "type": "STRING",
            //           "description": "location"
            //         }
            //       }
            //     }
            //   }
            //  """
            String jsonString =
                    "{\n"
                            + " \"name\": \"getCurrentWeather\",\n"
                            + " \"description\": \"Get the current weather in a given location\",\n"
                            + " \"parameters\": {\n"
                            + "   \"type\": \"OBJECT\", \n"
                            + "   \"properties\": {\n"
                            + "     \"location\": {\n"
                            + "       \"type\": \"STRING\",\n"
                            + "       \"description\": \"location\"\n"
                            + "     }\n"
                            + "   }\n"
                            + " }\n"
                            + "}";
            Tool tool =
                    Tool.newBuilder()
                            .addFunctionDeclarations(
                                    FunctionDeclarationMaker.fromJsonString(jsonString)
                            )
                            .build();

            // Start a chat session from a model, with the use of the declared
            // function.
            GenerativeModel model =
                    new GenerativeModel.Builder()
                            .setModelName(MODEL_NAME)
                            .setVertexAi(vertexAi)
                            .setTools(Arrays.asList(tool))
                            .build();
            ChatSession chat = model.startChat();

            System.out.println(String.format("Ask the question: %s", TEXT));
            GenerateContentResponse response = chat.sendMessage(TEXT);

            // The model will most likely return a function call to the declared
            // function `getCurrentWeather` with "Vancouver" as the value for the
            // argument `location`.
            System.out.println("\nPrint response: ");
            System.out.println(ResponseHandler.getContent(response));
            System.out.println("\n");

            // Provide an answer to the model so that it knows what the result of a
            // "function call" is.
            Content content =
                    ContentMaker.fromMultiModalData(
                            PartMaker.fromFunctionResponse(
                                    "getCurrentWeather", Collections.singletonMap("currentWeather", "snowing")));
            System.out.println("Provide the function response: ");
            System.out.println(content);
            System.out.println("\n");
            response = chat.sendMessage(content);

            // See what the model replies now
            System.out.println("\nPrint response: ");
            System.out.println(ResponseHandler.getText(response));
            System.out.println("\n");
        }
    }

}
