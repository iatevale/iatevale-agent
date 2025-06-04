package org.iatevale.config;

import com.google.auth.Credentials;

public record GCloudAuthParameters(String projectId, Credentials credentials) {
}
