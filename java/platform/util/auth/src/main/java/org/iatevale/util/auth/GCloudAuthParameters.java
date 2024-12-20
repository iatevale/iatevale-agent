package org.iatevale.util.auth;

import com.google.auth.Credentials;

public record GCloudAuthParameters(String projectId, Credentials credentials) {
}
