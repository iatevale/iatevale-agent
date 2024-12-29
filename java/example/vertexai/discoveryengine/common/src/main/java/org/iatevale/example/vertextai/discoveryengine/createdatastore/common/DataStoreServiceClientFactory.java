package org.iatevale.example.vertextai.discoveryengine.createdatastore.common;

import com.google.cloud.discoveryengine.v1.DataStoreServiceClient;
import com.google.cloud.discoveryengine.v1.DataStoreServiceSettings;
import org.iatevale.util.auth.GCloudAuthParameters;

import java.io.IOException;

public class DataStoreServiceClientFactory {

    static public DataStoreServiceClient create(GCloudAuthParameters parameters) throws IOException {
        return DataStoreServiceClient.create(settings(parameters));
    }

    static public DataStoreServiceSettings settings(GCloudAuthParameters parameters) throws IOException {

        final DataStoreServiceSettings.Builder builder = DataStoreServiceSettings.newBuilder()
                .setCredentialsProvider(parameters::credentials)
                .setQuotaProjectId(parameters.projectId());

        return builder.build();

    }

}
