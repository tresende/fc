package com.tresende.catalog.infrastructure.configuration;

import org.springframework.boot.autoconfigure.cache.Cache2kBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableCaching
public class CacheConfig {

    @Bean
    Cache2kBuilderCustomizer cache2kCustomizer() {
        return builder -> builder
                .entryCapacity(200)
                .build();
    }
}
