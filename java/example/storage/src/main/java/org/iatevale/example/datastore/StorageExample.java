package org.iatevale.example.datastore;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class Main {

    public static void main(String[] args) {
        // Instantiates a client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        System.out.println("Buckets:");
        for (Bucket bucket : storage.list().iterateAll()) {
            System.out.println(bucket.getName());
        }
        System.out.println("Application Name: " + System.getProperty("application.name"));
    }

}