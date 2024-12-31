#! /bin/bash
bazel run //java/product/iatevaleagent:iatevaleagent_gcloud_push
gcloud run jobs create iatevaleagent-job \
    --image=europe-west4-docker.pkg.dev/project-id/repo-name/iatevaleagent@sha256:80b076009f51a2c192ed89376958ff693ed018e6d69456da1b0e373bba49db23 \
    --project=project-id
gcloud run jobs execute iatevaleagent-job