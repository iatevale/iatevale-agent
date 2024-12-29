package org.iatevale.example.vertextai.discoveryengine.createdatastore;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.discoveryengine.v1.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class CreateDataStoreExample {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        // TODO(developer): Replace these variables before running the sample.
        // Project id.
        String projectId = "arctic-marking-424018-s2";
        // Unique identifier of the data store.
        String dataStoreId = "laprueba";
        // The display name of the data store.
        String dataStoreDisplayName = "Your Data Store Display Name";
        // The industry vertical that the data store registers.
        IndustryVertical industryVertical = IndustryVertical.INDUSTRY_VERTICAL_UNSPECIFIED;
        // The type of the data store.
        SolutionType solutionType = SolutionType.SOLUTION_TYPE_UNSPECIFIED;
        createDataStore(projectId, dataStoreId, dataStoreDisplayName, industryVertical, solutionType);
    }

    /**
     * Creates a data store in a given location.
     *
     * @param projectId the project id.
     * @param dataStoreId the unique identifier of the data store.
     * @param dataStoreDisplayName the display name of the data store.
     * @param industryVertical the industry vertical that the data store registers.
     * @param solutionType the type of the data store.
     * @throws IOException if the request fails.
     */
    public static void createDataStore(
            String projectId,
            String dataStoreId,
            String dataStoreDisplayName,
            IndustryVertical industryVertical,
            SolutionType solutionType)
            throws IOException, ExecutionException, InterruptedException {

        DataStoreServiceSettings dataStoreServiceSettings =
                DataStoreServiceSettings.newBuilder()
                        .build();

        try (DataStoreServiceClient dataStoreServiceClient =
                     DataStoreServiceClient.create(dataStoreServiceSettings)) {

            // Set the location id.
            String locationId = "global";
            // Set the collection id.
            String collectionId = "default_collection";
            // Set the parent path.
            String parent = "projects/" + projectId + "/locations/" + locationId + "/collections/" + collectionId;

            // Create a data store.
            DataStore dataStore =
                    DataStore.newBuilder()
                            .setDisplayName(dataStoreDisplayName)
                            .setIndustryVertical(industryVertical)
                            .addSolutionTypes(solutionType)
                            .setDefaultSchemaId("default_schema")
                            .build();

            // Create a request to create a data store.
            CreateDataStoreRequest request =
                    CreateDataStoreRequest.newBuilder()
                            .setParent(parent)
                            .setDataStore(dataStore)
                            .setDataStoreId(dataStoreId)
                            .build();

            // Execute the request synchronously.
            OperationFuture<DataStore, CreateDataStoreMetadata> response = dataStoreServiceClient.createDataStoreAsync(request);

            DataStore dataStore1 = response.get();

            // Print the response.
            System.out.println("Data store created successfully: " + response.getName());
        }
    }
}