package org.iatevale.example.datastore;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

public class DatastoreExample {

    public static void main(String... args) throws Exception {

        // Instancia el cliente de Datastore
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        // El tipo de entidad "Tarea"
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Tarea");

        // Crea una entidad
        Entity task = Entity.newBuilder(keyFactory.newKey("sampleTask"))
                .set("description", "Comprar leche")
                .set("done", false)
                .build();

        // Guarda la entidad en Datastore
        datastore.put(task);

        // Crea una consulta para obtener todas las tareas
        Query<Entity> query =
                Query.newEntityQueryBuilder()
                        .setKind("Tarea")
                        .setFilter(PropertyFilter.eq("done", false))
                        .build();

        // Ejecuta la consulta
        QueryResults<Entity> results = datastore.run(query);

        // Itera sobre los resultados
        while (results.hasNext()) {
            Entity result = results.next();
            System.out.println(result.getKey().getName() + ": " + result.getString("description"));
        }

        // Crea una transacción
        Transaction transaction = datastore.newTransaction();

        try {
            // Obtiene la tarea para actualizar
            Entity taskToUpdate = transaction.get(task.getKey());

            // Actualiza la tarea
            Entity updatedTask = Entity.newBuilder(taskToUpdate)
                    .set("done", true)
                    .build();

            // Guarda la tarea actualizada
            transaction.put(updatedTask);

            // Confirma la transacción
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }

        // Elimina la tarea
        datastore.delete(task.getKey());
    }

}