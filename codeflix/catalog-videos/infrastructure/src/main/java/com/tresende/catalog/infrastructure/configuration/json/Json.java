package com.tresende.catalog.infrastructure.configuration.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.concurrent.Callable;

public enum Json {
    INSTANCE;

    private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
            .dateFormat(new StdDateFormat())
            .featuresToDisable(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                    DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
                    SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
            )
            .modules(new JavaTimeModule(), new Jdk8Module(), afterburnerModule())
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();

    public static ObjectMapper mapper() {
        return INSTANCE.mapper.copy();
    }

    public static String writeValueAsString(final Object obj) {
        return invoke(() -> INSTANCE.mapper.writeValueAsString(obj));
    }

    public static <T> T readValue(final String string, final Class<T> clazz) {
        return invoke(() -> INSTANCE.mapper.readValue(string, clazz));
    }

    private static <T> T invoke(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AfterburnerModule afterburnerModule() {
        final var module = new AfterburnerModule();
        module.setUseValueClassLoader(false);
        return module;
    }
}
