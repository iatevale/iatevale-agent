package org.iatevale.config;

import com.google.auth.oauth2.GoogleCredentials;

public record GCloudAuthParameters(String projectId, GoogleCredentials credentials) {
}
