package com.tresende.catalog.infrastructure.authentication;

import com.tresende.catalog.AbstractRestClientTest;
import com.tresende.catalog.infrastructure.authentication.KeycloakAuthenticationGateway.KeycloakAuthenticationResult;
import com.tresende.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class KeycloakAuthenticationGatewayTest extends AbstractRestClientTest {

    @Autowired
    private KeycloakAuthenticationGateway gateway;

    @Test
    public void givenValidParams_whenCallsLogin_thenShouldReturnClientCredentials() {
        //given
        final var expectedClientId = "client123";
        final var expectedClientSecret = "secret123";
        final var expectedAccessToken = "access_token_abc";
        final var expectedRefreshToken = "refresh_token_xyz";

        stubFor(
                post(urlPathEqualTo("/realms/test/protocol/openid-connect/token"))
                        .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(Json.writeValueAsString(new KeycloakAuthenticationResult(expectedAccessToken, expectedRefreshToken)))
                        )
        );

        //when
        final var actualOutput = gateway.login(
                new AuthenticationGateway.ClientCredentialsInput(expectedClientId, expectedClientSecret)
        );

        //then
        Assertions.assertEquals(expectedAccessToken, actualOutput.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualOutput.refreshToken());
    }
}