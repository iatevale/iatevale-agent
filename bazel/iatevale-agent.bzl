load("@rules_jvm_external//:defs.bzl", "maven_install")

def iatevale_agent_git_repo():
    git_repository(
        name = "googleapis",
        commit = GOOGLE_APIS_COMMIT,
        remote = "https://github.com/googleapis/googleapis.git",
        verbose = False,
    )

    git_repository(
        name = "io_grpc_grpc_java",
        remote = "https://github.com/grpc/grpc-java.git",
        tag = "v1.60.0",
        verbose = False,
    )

def iatevale_agent_java_deps():
    maven_install(
        artifacts = [
            "com.google.guava:guava:33.0.0-jre",
            "org.json:json:20210307",
            "org.assertj:assertj-core:3.4.1",
            "org.hamcrest:hamcrest:2.2",
            "com.google.cloud:google-cloud-datastore:2.14.1",
            "com.google.cloud:google-cloud-aiplatform:3.56.0",
            "com.google.inject:guice:6.0.0",
            "com.moandjiezana.toml:toml4j:0.7.2",
            "com.google.code.gson:gson:2.10.1",
            "org.slf4j:slf4j-api:2.0.7",
            "org.slf4j:slf4j-jdk14:2.0.7",
        ],
        repositories = [
            "https://repo1.maven.org/maven2",
        ],
    )
