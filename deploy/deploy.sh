#! /bin/bash
bazel run //deploy:datastore_example_gcloud_push
gcloud run deploy datastore-example-service \
    --image=gcr.io/<TU_PROYECTO_ID>/datastore-example-image \
    --platform=managed \
    --allow-unauthenticated