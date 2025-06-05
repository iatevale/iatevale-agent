package org.iatevale.adk.common.model;

import com.google.adk.models.Gemini;
import com.google.adk.models.VertexCredentials;
import org.iatevale.config.GCloudAuthParameters;
import org.iatevale.config.IATevaleConfig;

public class GeminiModelUtil {

    final static public String MODEL_NAME = "gemini-2.5-flash-preview-05-20";

    static public Gemini create() {

        final GCloudAuthParameters gCloudAuthParameters = IATevaleConfig.getGCloudAuthParameters();

        final VertexCredentials vertexCredentials = VertexCredentials.builder()
                .setProject(gCloudAuthParameters.projectId())
                .setCredentials(gCloudAuthParameters.credentials())
                .build();

        return Gemini.builder()
                .modelName(MODEL_NAME)
                .vertexCredentials(vertexCredentials)
                .build();

    }
}
