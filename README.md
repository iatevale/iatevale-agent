# iatevale-agent

El objetivo de este repositorio es hacer todo tipo de pruebas con la IA Generative de Gemini
para explorar su uso practico integrandola en otros desarrollos.

Por tanto es puramente especulativo y puede sufrir refactorizaciones drasticas. 

Para ser productivos se utilizaran los productos de gcloud y por tanto estos productos
tambien seran parte del proceso de aprendizaje.

El sistema de build utilizado es [BAZEL](https://bazel.build/) ya que este es el sistema nativo de los productos de Google.

## Estructura del repositorio

| Directorio     | Contenido                 |
|----------------|---------------------------|
| /bazel         | Scripts bazel en Starlark |
| /deploy | Scripts de despliega de algunos productos en GCloud |
| /java          | Codigo java               |
| /java/platform | Componentes que se pueden cablear con los productos mediante inyeccion |
| /java/products | Productos generados |
| /java/examples | Ejemplos de usos de las tecnologias |
| /proto         | Ficheros Protobuf |

## Librerias

### La esencia
El nivel mas bajo de acceso a los productos GCloud es mediante tecnologia protobus/grpc
y estan todas las definiciones [en este repositorio](https://github.com/googleapis/googleapis).

Este nivel requiere generar el codigo del lenguaje correspondiente mediante Bazel.

### Documentacion de entrada

La pagina principal es [esta](https://cloud.google.com/apis)

### Especificamente para java

La documentacion del reposotio https://github.com/googleapis/google-cloud-java contiene
una tabla que nos permite acceder a cada libreria en concreto.

Algunas librerias interesantes para las pruebas:

| Reositorio                                     | Descripcion                                                            |
|------------------------------------------------|------------------------------------------------------------------------|
| https://github.com/googleapis/java-datastore | Google Cloud Datastore Client for Java |
| https://github.com/googleapis/java-storage-nio | Google NIO Filesystem Provider for Google Cloud Storage Client for Java                                             |
| https://github.com/googleapis/java-storage     |Google Cloud Storage Client for Java                    |

Para AI Generativa tienen tu propia portal -> [aqui](https://ai.google.dev/)

Aqui encontramos la [API para Gemini](https://ai.google.dev/gemini-api)

Aunque tambien sugieren que para utilizar nustros propios datos utilizemoe [Gemmma](https://ai.google.dev/gemma)
```
Own your AI with Gemma open models
Build custom AI solutions and retain complete control. Tailor Gemma models, built from the same research and technology as Gemini, with your own data.
En cada uno de lso repositorios hay documentacion y ejemplos.
```

**En fin, aqui empieza el lio de verdad...**

## Agente **iatevale-agent**

### Funcionalidades

La idea es que nos permita realizar experimentos de interaccion con Gemini 

### Despliegue en en gcloud cloud run 

de aplicaciones en gcloud cloud run

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
