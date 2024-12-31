# iatevale-agent

El objetivo de este repositorio es hacer todo tipo de pruebas con la IA Generative de Gemini
para explorar su uso practico integrandola en otros desarrollos.

Por tanto es puramente especulativo y puede sufrir refactorizaciones drasticas. 

Para ser productivos se utilizaran los productos de gcloud y por tanto estos productos
tambien seran parte del proceso de aprendizaje.

El sistema de build utilizado es [BAZEL](https://bazel.build/) (con el nuevo sistema de dependencias por modulos) ya que este es el sistema nativo de los productos de Google.

## La web mas importante

[Pagina oficial](https://ai.google.dev)

## Librerias

### La esencia
El nivel mas bajo de acceso a los productos GCloud es mediante tecnologia protobus/grpc
y estan todas las definiciones [en este repositorio](https://github.com/googleapis/googleapis).

Este nivel requiere generar el codigo del lenguaje correspondiente mediante Bazel.

### Documentacion de entrada

La pagina principal es [esta](https://cloud.google.com/apis)

Documetacion de APIs especifica para IA:

* [API para IA](https://ai.google.dev/api)

* [API para Gemini](https://ai.google.dev/gemini-api)

* [Cookbook](https://github.com/google-gemini/cookbook)

Aunque tambien sugieren que para utilizar nuestros propios datos utilizemoe [Gemmma](https://ai.google.dev/gemma)
```
Own your AI with Gemma open models
Build custom AI solutions and retain complete control. 
Tailor Gemma models, built from the same research and technology as Gemini, with your own data.
```

### Librerias para java

Hay bastante lio con las librerias para java asi que simplemente se lista una lista de las direcciones que resultan de interes.

https://cloud.google.com/vertex-ai/generative-ai

https://cloud.google.com/java/docs

https://cloud.google.com/java/docs/reference

https://github.com/googleapis/sdk-platform-java

https://github.com/googleapis/google-cloud-java

https://github.com/googleapis/google-cloud-java/tree/main/java-vertexai

https://cloud.google.com/java/docs/reference/google-cloud-aiplatform/latest/overview

https://github.com/googleapis/google-cloud-java/tree/main/java-aiplatform/samples/snippets/generated/com/google/cloud/aiplatform/v1

https://github.com/google-gemini/generative-ai-android?tab=readme-ov-file



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

## Ejemplos

Estos ejemplos estan pensados para ejecutarse sobre infraestructura GCloud por lo que 
hay que tener acceso a un projecto GCloud.

En principio estos ejemplos no tienen que generar costo o sera insignificante.

Los ejemplos de verificacion de funcionamiento de Datastore y Storage funcionan.

Los ejemplos de vertexai estan dejados caer y solo he ajustado el codigo de 01 para que funcione.

La idea es ir ajustando los ejemplos poco a poco ya que cada uno aporta un poquito mas respecto al siguiente
en lo referente al uso de la API.

Supongo que despues se pueden ir generando mas ejemplos para ir verificando nuevas funcionalidades...

Finalmente llegado el momento tal vez construir algun tipo de agente que se comunique 
con nosotros a traves de la implementacion de un [bot telegram...](https://monsterdeveloper.gitbook.io/java-telegram-bot-tutorial)

De momento lo que es el agente que se puede cargar en un Cloud run es un simple printf cada ciertos segundos...

La cuestion es disponer de una ARENA donde ir haciendo pruebas....

### Configuracion para GCloud

Se necesitan los siguientes datos:

* Una cuenta de servicio
* Una credencial de tipo JSON

Hay que crear un fichero ".iatevale/config.propertie" dentro del home del usuario linux.

La credencial JSON tambien se puede dejay en el directorio ".iatevale".

Ejemplo de fichero $HOME/.iatevale/config.properties
```
project.id=project-name
credentials=/home/{username}/.iatevale/lacredencial.json
```

La lista de Roles que tiene que tener asignado la cuenta de servicio:
* Administrador de almacenamiento
* Usuario de Cloud Datastore
* Usuario de Vertex AI

### Despliegue de un agente gcloud cloud run 

En este despliegue se supone que ya se ha creado un proyecto gcloud.

El primer paso es tener [instaladas](https://cloud.google.com/sdk/docs/install-sdk?hl=es-419) las utilidades cliente de gcloud, una vez instaladas lanzamos el comando **gcloud init** para especificar el entorno de trabajo.

En el proyecto gcloud hemos de crear un repositorio de Artifact Registry donde se subirá la imagen dockerizada que queremos desplegar. Entre las opciones de creación elegiremos formato docker y modo estándar.

Una vez creado el repositorio hemos de ajustar la regla bazel que encontraremos en el fichero BUILD.bazel del producto que vayamos a desplegar. En la línea repository de la regla que adjunto a continuación hay que substituir project-id y repo-name por el id de proyecto gcloud y el nombre del repositorio de artifacts que hemos creado en el proyecto

BUILD.bazel:
```
oci_push(
    name = "iatevaleagent_gcloud_push",
    image = ":iatevaleagent_gcloud_image",
    repository = "europe-west4-docker.pkg.dev/project-id/repo-name/iatevaleagent",
)
```

El fichero deploy.sh es un script que se encarga de llamar a la regla bazel anterior que subirá la imagen a un repositorio de artefactos y la dejará disponible para que se pueda desplegar en un servicio de gcloud cloud run. 
Antes de ejecutarlo se deben de cambiar las mismas variables que en el caso anterior y se debe de incluir el hash de la imagen subida en el repositorio.

También dse debera de ejecutar **gcloud auth configure-docker europe-west4-docker.pkg.dev**

deploy.sh:
```
#! /bin/bash
bazel run //java/product/iatevaleagent:iatevaleagent_gcloud_push
gcloud run jobs create iatevaleagent-job \
    --image=europe-west4-docker.pkg.dev/project-id/repo-name/iatevaleagent@sha256:80b076009f51a2c192ed89376958ff693ed018e6d69456da1b0e373bba49db23 \
    --project=project-id
gcloud run jobs execute iatevaleagent-job
```

Si funciona correctamente en nuestro proyecto gcloud en cloud run en la pestaña jobs estará ejecutándose nuestro trabajo y en registros podremos ver el log.