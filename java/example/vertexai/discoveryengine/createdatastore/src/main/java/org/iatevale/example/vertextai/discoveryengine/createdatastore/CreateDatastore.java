package org.iatevale.example.vertextai.discoveryengine.createdatastore;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.discoveryengine.v1.*;
import org.iatevale.example.vertextai.discoveryengine.createdatastore.common.DataStoreServiceClientFactory;

import java.io.IOException;

public class CreateDatastore {

    public static void main(String[] args) throws IOException {
        createDataStore();
    }

    public static void createDataStore() throws IOException {

        try (DataStoreServiceClient dataStoreServiceClient = DataStoreServiceClientFactory.create()) {

            // Define el Data Store que quieres crear
            DataStore dataStore = DataStore.newBuilder()
                    .setDisplayName("Mi Nuevo Data Store IoT") // Nombre para mostrar en la consola
                    .addSolutionTypes(SolutionType.SOLUTION_TYPE_CHAT)
                    .addSolutionTypes(SolutionType.SOLUTION_TYPE_GENERATIVE_CHAT)
                    .build();

            CreateDataStoreRequest request = CreateDataStoreRequest.newBuilder()
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
            System.out.println("Tipo de solución por defecto: " + response.getSolutionTypesList().toString());

        } catch (Exception e) {
            System.err.println("Error al crear el Data Store: " + e);
        }

    }
}