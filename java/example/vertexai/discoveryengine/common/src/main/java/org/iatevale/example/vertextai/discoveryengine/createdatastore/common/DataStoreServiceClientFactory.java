package org.iatevale.example.vertextai.discoveryengine.createdatastore.common;

import com.google.cloud.discoveryengine.v1.DataStoreServiceClient;
import com.google.cloud.discoveryengine.v1.DataStoreServiceSettings;
import org.iatevale.util.auth.GCloudAuthFactory;
import org.iatevale.util.auth.GCloudAuthParameters;

import java.io.IOException;

public class DataStoreServiceClientFactory {

    static public DataStoreServiceClient create() throws IOException {
        return DataStoreServiceClient.create(settings());
    }

    static public DataStoreServiceSettings settings() throws IOException {

        final GCloudAuthParameters parameters = GCloudAuthFactory.getParameters();

        final DataStoreServiceSettings.Builder builder = DataStoreServiceSettings.newBuilder()
                .setCredentialsProvider(parameters::credentials)
                .setQuotaProjectId(parameters.projectId());

        return builder.build();

    }

}
