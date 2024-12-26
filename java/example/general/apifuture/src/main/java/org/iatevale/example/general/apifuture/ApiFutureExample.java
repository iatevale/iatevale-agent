package org.iatevale.example.general.apifuture;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.common.util.concurrent.FutureCallback;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiFutureExample {

    public static void main(String[] args) throws Exception {
        // Crear un ExecutorService para ejecutar las operaciones asíncronas.
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 1. Realizar una operación asíncrona que simula una consulta a una base de datos.
        ApiFuture<String> queryResult = queryDatabaseAsync("SELECT name FROM users WHERE id = 1", executor);

        // 2. Añadir un callback que se ejecutará cuando la operación se complete.
        final FutureCallback<String> futureCallback = new FutureCallback<>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("Consulta exitosa. Resultado: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.err.println("Error en la consulta: " + t.getMessage());
            }
        };
        ApiFutures.addCallback(queryResult, futureCallback, executor);

        // 3. Obtener el resultado de la operación asíncrona de forma no bloqueante (opcional).
        //    En este punto, el hilo principal puede continuar con otras tareas.
        System.out.println("Realizando otras tareas mientras la consulta se ejecuta...");

        // 4. Manejar posibles excepciones durante la operación asíncrona (con get() bloqueante).
        //    Este bloque se puede omitir si se maneja la excepción en el callback onFailure.
        try {
            String result = queryResult.get(); // Bloquea hasta que el resultado esté disponible.
            System.out.println("Resultado obtenido con get(): " + result);
        } catch (Exception e) {
            System.err.println("Error al obtener el resultado con get(): " + e.getMessage());
        }

        // 5. Utilizar ApiFutures para transformar y combinar resultados.

        // Simular dos consultas más.
        ApiFuture<Integer> userAge = queryDatabaseAsync("SELECT age FROM users WHERE id = 1", executor).transform(Integer::parseInt, executor);
        ApiFuture<String> userCity = queryDatabaseAsync("SELECT city FROM users WHERE id = 1", executor);

        // Transformar el resultado de una consulta.
        ApiFuture<String> userNameUppercase = queryResult.transform(String::toUpperCase, executor);
        System.out.println("Nombre en mayúsculas (transform): " + userNameUppercase.get());

        // Combinar los resultados de varias consultas en una lista.
        List<ApiFuture<?>> futures = Arrays.asList(queryResult, userAge, userCity);
        ApiFuture<List<Object>> combinedFuture = ApiFutures.allAsList(futures);

        // Transformar la lista de resultados en una cadena.
        ApiFuture<String> combinedResultString =
                combinedFuture.transform(
                        results -> {
                            String name = (String) results.get(0);
                            int age = (int) results.get(1);
                            String city = (String) results.get(2);
                            return String.format("Nombre: %s, Edad: %d, Ciudad: %s", name, age, city);
                        },
                        executor);

        System.out.println("Resultado combinado: " + combinedResultString.get());

        // Utilizar ApiFutures.successfulAsList para manejar fallos individuales en una lista de Futures
        ApiFuture<Integer> failedFuture = queryDatabaseAsync("SELECT invalid_column FROM users WHERE id = 1", executor).transform(Integer::parseInt, executor);
        List<ApiFuture<?>> futuresWithFailure = Arrays.asList(queryResult, userAge, userCity, failedFuture);
        ApiFuture<List<Object>> successfulAsListFuture = ApiFutures.successfulAsList(futuresWithFailure);

        ApiFuture<String> successfulAsListResultString =
                successfulAsListFuture.transform(
                        results -> {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < results.size(); i++) {
                                Object result = results.get(i);
                                if (result != null) {
                                    sb.append("Resultado ").append(i + 1).append(": ").append(result).append(", ");
                                } else {
                                    sb.append("Resultado ").append(i + 1).append(": Fallido, ");
                                }
                            }
                            return sb.toString();
                        },
                        executor);

        System.out.println("Resultado con successfulAsList: " + successfulAsListResultString.get());

        // Cerrar el ExecutorService.
        executor.shutdown();
    }

    // Función que simula una consulta asíncrona a una base de datos.
    private static <T> ApiFuture<T> queryDatabaseAsync(String query, ExecutorService executor) {
        return ApiFutures.submit(
                new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        // Simular un retraso de la consulta.
                        Thread.sleep(1000);

                        // Simular diferentes resultados basados en la consulta.
                        if (query.contains("invalid_column")) {
                            throw new Exception("Columna inválida en la consulta.");
                        } else if (query.contains("name")) {
                            return (T) "John Doe";
                        } else if (query.contains("age")) {
                            return (T) Integer.valueOf(30);
                        } else if (query.contains("city")) {
                            return (T) "New York";
                        } else {
                            throw new Exception("Consulta no reconocida.");
                        }
                    }
                },
                executor);
    }
}
