package com.tresende.catalog.infrastructure.authentication;

import com.tresende.catalog.infrastructure.authentication.AuthenticationGateway.AuthenticationResult;
import com.tresende.catalog.infrastructure.authentication.AuthenticationGateway.ClientCredentialsInput;
import com.tresende.catalog.infrastructure.configuration.properties.KeycloakProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

@Component
public class ClientCredentialsManager implements GetClientCredentials, RefreshClientCredentials {

    private static final AtomicReferenceFieldUpdater<ClientCredentialsManager, ClientCredentials> UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(ClientCredentialsManager.class, ClientCredentials.class, "credentials");
    private static final Logger log = LoggerFactory.getLogger(ClientCredentialsManager.class);

    private final AuthenticationGateway authenticationGateway;
    private final KeycloakProperties keycloakProperties;
    private volatile ClientCredentials credentials;


    public ClientCredentialsManager(
            final AuthenticationGateway authenticationGateway,
            final KeycloakProperties keycloakProperties) {

        this.authenticationGateway = Objects.requireNonNull(authenticationGateway);
        this.keycloakProperties = Objects.requireNonNull(keycloakProperties);
    }

    @Override
    public String retrieve() {
        return credentials.accessToken;
    }

    @Override
    public void refresh() {
        final var result = this.credentials == null ? login() : refreshToken();
        UPDATER.set(this, new ClientCredentials(result.accessToken(), result.refreshToken(), clientId()));
    }

    private AuthenticationResult login() {
        return this.authenticationGateway.login(
                new ClientCredentialsInput(clientId(), clientSecret())
        );
    }

    private AuthenticationResult refreshToken() {
        try {
            return this.authenticationGateway.refresh(
                    new AuthenticationGateway.RefreshTokenInput(clientId(), clientSecret(), credentials.refreshToken)
            );
        } catch (RuntimeException e) {
            return login();
        }
    }

    private String clientId() {
        return keycloakProperties.clientId();
    }

    private String clientSecret() {
        return keycloakProperties.clientSecret();
    }

    record ClientCredentials(String clientId, String accessToken, String refreshToken) {
    }
}
