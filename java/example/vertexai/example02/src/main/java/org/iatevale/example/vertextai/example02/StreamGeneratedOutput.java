package org.iatevale.example.vertextai.example02;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import org.iatevale.example.vertextai.common.VertextaiUtil;
import org.iatevale.util.conversion.ProtoUtils;

import java.io.IOException;

// Continacion del example01
//
// Lo interesate es que llama al mismo servicio pero el retorno es un stream de resultados....
// Aunque alfinal lo convierte en una llamada unitaria (convierte el stream que puede ser infinito en una lista finita)
// service GenerativeService {
//   rpc GenerateContent(GenerateContentRequest) returns (stream GenerateContentResponse)
//   ..
// }
public class StreamGeneratedOutput {

    public static void main(String[] args) throws IOException {

        try (VertexAI vertexAi = VertextaiUtil.vertexBuilder().build()) {

            final GenerativeModel model = new GenerativeModel("gemini-pro", vertexAi);

            final ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream("How are you?");

            for (GenerateContentResponse response : responseStream) {
                System.out.println("-------------------------------------");
                System.out.println(ProtoUtils.messageToJson(response));
            }

        }
    }

}
