package com.tresende.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.tresende.catalog.infrastructure.configuration.WebServerConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
@Tag("integrationTest")
@AutoConfigureWireMock(port = 0)
public class AbstractRestClientTest {

    @Autowired
    public CacheManager cacheManager;

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
        resetAllCaches();
        resetFaultTolerance();
    }

    protected void resetAllCaches() {
        cacheManager.getCacheNames().forEach(this::clearCache);
    }

    protected void clearCache(final String key) {
        final var value = cache(key);
        if (value != null) {
            cache(key).clear();
        }
    }

    protected Cache cache(final String name) {
        return cacheManager.getCache(name);
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
