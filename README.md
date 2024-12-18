# iatevale-agent


### Despliegue de aplicaciones en gcloud cloud run

En el directorio deploy existe dos ficheros encargados para la construcción y despliegue de aplicaciones.

El fichero BUILD.bazel Incluye las reglas necesarias para construir una imagen dockerizada y subirla al repositorio de artefactos del proyecto gcloud.


BUILD.bazel:
```load("@rules_oci//oci:defs.bzl", "oci_image")
load("@rules_oci//oci:defs.bzl", "oci_push")
load("@rules_pkg//pkg:tar.bzl", "pkg_tar")


# Empaqueta la construcción del jar de la aplicación
pkg_tar(
    name = "deploy_tar",
    srcs = ["//java:datastore-example_deploy.jar"],
)

# Construye el container docker incluyendo el jar de la imagen anterior
oci_image(
    name = "datastore_example_gcloud_image",
    base = "@java_21_base",
    entrypoint = [
        "java",
        "-jar",
        "datastore-example_deploy.jar",
    ],
    tars = [":deploy_tar"],
    visibility = ["//visibility:public"],
)

#### IMPORTANTE: en el atributo repository cambiar project-id y repo-name por las variables que proceda.
# Sube al repositorio de artefactos del proyecto gcloud indicado la imagen generada
oci_push(
    name = "datastore_example_gcloud_push",
    image = ":datastore_example_gcloud_image",
    repository = "europe-west3-docker.pkg.dev/project-id/repo-name/datastore-example",
)
```

El fichero deploy.sh es un script que se encarga de llamar a la regla bazel anterior que subira la imagen a un repositorio de artefactos y la dejara disponible para que se pueda desplegar en un servicio de gcloud cloud run.

deploy.sh:
```
#! /bin/bash
bazel run //deploy:datastore_example_gcloud_push
gcloud run deploy datastore-example-service \
    --image=gcr.io/<TU_PROYECTO_ID>/datastore-example-image \
    --platform=managed \
    --allow-unauthenticat
```
