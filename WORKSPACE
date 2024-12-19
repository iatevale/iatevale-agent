load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

# No es necesario si estas usando modulos Bazel
# load("@googleapis//:repository.bzl", "googleapis_repository")
# googleapis_repository()

load("@rules_oci//oci:repositories.bzl", "oci_register_toolchains")

oci_register_toolchains()

load("//bazel:iatevale_agent_deps.bzl", "iatevale_agent_deps")

iatevale_agent_deps()
