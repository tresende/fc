package com.tresende.catalog.infrastructure.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tresende.catalog.domain.exceptions.InternalErrorException;
import com.tresende.catalog.infrastructure.configuration.annotations.Keycloak;
import com.tresende.catalog.infrastructure.configuration.properties.KeycloakProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
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
        final var map = new LinkedMultiValueMap();

        map.add("grant_type", "client_credentials");
        map.add("client_id", input.clientId());
        map.add("client_secret", input.clientSecret());

        final var output = this.restClient.post()
                .uri(tokenUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(map)
                .retrieve()
                .body(KeycloakAuthenticationResult.class);

        if (output == null)
            throw InternalErrorException.with("Failed to create client credentials [clientId: %s]".formatted(input.clientId()));

        return new AuthenticationResult(output.accessToken, output.refreshToken);
    }

    @Override
    public AuthenticationResult refresh(final RefreshTokenInput input) {
        final var map = new LinkedMultiValueMap();

        map.add("grant_type", "refresh_token");
        map.add("client_id", input.clientId());
        map.add("client_secret", input.clientSecret());
        map.add("refresh_token", input.refreshToken());

        final var output = this.restClient.post()
                .uri(tokenUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(map)
                .retrieve()
                .body(KeycloakAuthenticationResult.class);

        if (output == null)
            throw InternalErrorException.with("Failed to refresh client credentials [clientId: %s]".formatted(input.clientId()));

        return new AuthenticationResult(output.accessToken, output.refreshToken);
    }

    public record KeycloakAuthenticationResult(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("refresh_token") String refreshToken
    ) {
    }
}
