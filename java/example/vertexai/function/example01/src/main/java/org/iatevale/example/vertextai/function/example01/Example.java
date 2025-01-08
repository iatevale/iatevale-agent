package org.iatevale.example.vertextai.function.example01;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.iatevale.example.vertextai.common.VertextaiUtil;
import org.iatevale.util.conversion.ProtoUtils;

import java.io.IOException;

public class Example {

    static final private String MODEL_NAME = "gemini-pro";

    public static void main(String[] args) throws IOException {

        try (VertexAI vertexAi = VertextaiUtil.vertexBuilder().build()) {
            final GenerativeModel model = new GenerativeModel(MODEL_NAME, vertexAi);
            final GenerateContentResponse response = model.generateContent("How are you?");
            System.out.println(ProtoUtils.messageToJson(response));
        }
    }

}