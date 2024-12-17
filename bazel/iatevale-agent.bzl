load("@rules_jvm_external//:defs.bzl", "maven_install")

def iatevale_agent_java_deps():
    maven_install(
        artifacts = [
            "org.json:json:20210307",
            "org.assertj:assertj-core:3.4.1",
            "org.hamcrest:hamcrest:2.2",
        ],
        repositories = [
            "https://repo1.maven.org/maven2",
        ],
    )
