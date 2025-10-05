package com.tresende.catalog.infrastructure.authentication;

import com.tresende.catalog.infrastructure.configuration.annotations.Keycloak;
import com.tresende.catalog.infrastructure.configuration.properties.KeycloakProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class KeycloakAuthenticationGateway implements AuthenticationGateway {

    private final RestClient restClient;
    private final String tokenUri;

    public KeycloakAuthenticationGateway(
            @Keycloak final RestClient restClient,
            final KeycloakProperties keycloakProperties
    ) {
        this.restClient = Objects.requireNonNull(restClient);
        this.tokenUri = keycloakProperties.tokenUri();
    }

    @Override
    public AuthenticationResult login(final ClientCredentialsInput input) {
        return null;
    }

    @Override
    public AuthenticationResult refresh(final RefreshTokenInput input) {
        return null;
    }
}
