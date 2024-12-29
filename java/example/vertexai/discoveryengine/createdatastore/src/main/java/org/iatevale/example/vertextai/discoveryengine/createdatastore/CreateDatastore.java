package org.iatevale.example.vertextai.discoveryengine.createdatastore;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.discoveryengine.v1.*;
import java.io.IOException;

public class CreateDataStore {

    public static void main(String[] args) throws IOException {

        // Reemplaza con tu ID de proyecto y ubicación
        String projectId = "tu-proyecto-id";
        String location = "tu-ubicacion"; // Por ejemplo, "us-central1"

        createDataStore(projectId, location);
    }

    public static void createDataStore(String projectId, String location) throws IOException {
        try (DataStoreServiceClient dataStoreServiceClient = DataStoreServiceClient.create()) {
            String parent = String.format("projects/%s/locations/%s", projectId, location);

            // Define el Data Store que quieres crear
            DataStore dataStore = DataStore.newBuilder()
                    .setDisplayName("Mi Nuevo Data Store IoT") // Nombre para mostrar en la consola
                    .setDefaultSolutionType(SolutionType.SEARCH) // Tipo de solución: SEARCH, RECOMMENDATION, o CHAT
                    // Puedes configurar otros parámetros aquí, como industry_vertical
                    .build();

            CreateDataStoreRequest request = CreateDataStoreRequest.newBuilder()
                    .setParent(parent)
                    .setDataStore(dataStore)
                    .build();

            // Crea el Data Store (esta operación es asíncrona)
            OperationFuture<DataStore, CreateDataStoreMetadata> future =
                    dataStoreServiceClient.createDataStoreOperationCallable().futureCall(request);

            System.out.println("Solicitud de creación de Data Store enviada. Esperando a que se complete...");

            // Espera a que la operación se complete
            DataStore response = future.get();

            System.out.println("Data Store creado exitosamente:");
            System.out.println("Nombre: " + response.getName());
            System.out.println("Nombre para mostrar: " + response.getDisplayName());
            System.out.println("Tipo de solución por defecto: " + response.getDefaultSolutionType());

        } catch (Exception e) {
            System.err.println("Error al crear el Data Store: " + e);
        }
        
    }
}