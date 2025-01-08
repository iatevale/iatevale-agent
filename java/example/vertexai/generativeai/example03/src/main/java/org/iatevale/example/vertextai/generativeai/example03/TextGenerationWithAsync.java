package org.iatevale.example.vertextai.generativeai.example03;

import com.google.api.core.ApiFuture;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.iatevale.example.vertextai.common.VertextaiUtil;
import org.iatevale.util.conversion.ProtoUtils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

// Este ejemplo a diferencia de los anteriores utiliza los STUB asincronos java que genera el GRPC para
// poder consumir el resultado de forma asincrona.

public class TextGenerationWithAsync {

    static final private String MODEL_NAME = "gemini-pro";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        try (VertexAI vertexAi = VertextaiUtil.vertexBuilder().build()) {

            final GenerativeModel model = new GenerativeModel(MODEL_NAME, vertexAi);

            final ApiFuture<GenerateContentResponse> future = model.generateContentAsync("How are you?");

            // Hacemos algo que resulte bloqueante...
            TimeUnit.SECONDS.sleep(4);

            // Get the response from Future
            final GenerateContentResponse response = future.get();

            // Seguro que tenemos el resultado ya preparado y si no espermos..
            System.out.println(ProtoUtils.messageToJson(response));
        }

    }

}
