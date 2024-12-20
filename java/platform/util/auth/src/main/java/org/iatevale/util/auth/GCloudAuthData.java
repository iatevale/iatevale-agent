package org.iatevale.util.auth;

import com.google.auth.Credentials;

public record GCloudAuthData(String projectId, Credentials credentials) {
}
