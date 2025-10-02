package com.tresende.catalog.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.Cache2kBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableCaching
public class CacheConfig {

    @Bean
    Cache2kBuilderCustomizer cache2kCustomizer(
            @Value("${cache.ttl:60}") final int ttl,
            @Value("${cache.max-entries:200}") final int maxEntries
    ) {
        return builder -> builder
                .entryCapacity(maxEntries)
                .expireAfterWrite(ttl, TimeUnit.SECONDS)
                .build();
    }
}
