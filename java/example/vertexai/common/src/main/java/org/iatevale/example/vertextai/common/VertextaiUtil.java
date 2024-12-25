package org.iatevale.example.vertextai.common;

import com.google.cloud.vertexai.VertexAI;
import org.iatevale.util.auth.GCloudAuthFactory;
import org.iatevale.util.auth.GCloudAuthParameters;

import java.io.IOException;

public class VertextaiUtil {

    static public VertexAI.Builder vertexBuilder() throws IOException {
        final GCloudAuthParameters parameters = GCloudAuthFactory.getParameters();
        return new VertexAI.Builder()
                .setProjectId(parameters.projectId())
                .setCredentials(parameters.credentials());
    }

}
