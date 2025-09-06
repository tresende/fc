package com.tresende.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.tresende.catalog.infrastructure.configuration.WebServerConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-integration")
@EnableAutoConfiguration(exclude = {
        KafkaAutoConfiguration.class,
        ElasticsearchRepositoriesAutoConfiguration.class
})
@SpringBootTest(classes = {
        WebServerConfig.class,
        IntegrationTestConfiguration.class
})
@AutoConfigureWireMock(port = 0)
public class AbstractRestClientTest {


    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    BulkheadRegistry bulkheadRegistry;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    @BeforeEach
    void beforeEach() {
        WireMock.reset();
        WireMock.resetAllRequests();
        resetFaultTolerance();
    }

    protected void resetFaultTolerance() {
        circuitBreakerRegistry.getAllCircuitBreakers()
                .forEach(CircuitBreaker::reset);
    }

    protected void assertCircuitBreakerState(final String name, final CircuitBreaker.State expectedState) {
        final var cb = circuitBreakerRegistry.circuitBreaker(name);
        Assertions.assertEquals(expectedState, cb.getState());
    }

    public void transitionToOpenState(final String name) {
        final var cb = circuitBreakerRegistry.circuitBreaker(name);
        cb.transitionToOpenState();
    }

    public void transitionToClosedState(final String name) {
        final var cb = circuitBreakerRegistry.circuitBreaker(name);
        cb.transitionToClosedState();
    }

    protected void acquireBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).acquirePermission();
    }

    protected void releaseBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).releasePermission();
    }

    public String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
