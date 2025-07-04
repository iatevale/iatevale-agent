package org.iatevale.example.storage;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.iatevale.util.auth.GCloudAuthFactory;
import org.iatevale.util.auth.GCloudAuthParameters;

import java.io.IOException;

public class StorageExample {

    public static void main(String[] args) throws IOException {

        final GCloudAuthParameters parameters = GCloudAuthFactory.getParameters();

        final Storage storage = StorageOptions.newBuilder()
                .setProjectId(parameters.projectId())
                .setCredentials(parameters.credentials())
                .build()
                .getService();

        System.out.println("Buckets:");
        for (Bucket bucket : storage.list().iterateAll()) {
            System.out.println(bucket.getName());
        }
    }

}