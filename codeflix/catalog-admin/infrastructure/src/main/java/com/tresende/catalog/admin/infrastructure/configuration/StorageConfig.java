package com.tresende.catalog.admin.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.tresende.catalog.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.tresende.catalog.admin.infrastructure.configuration.properties.storage.StorageProperties;
import com.tresende.catalog.admin.infrastructure.services.StorageService;
import com.tresende.catalog.admin.infrastructure.services.impl.GCStorageService;
import com.tresende.catalog.admin.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalog-admin")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean
    @Profile({"development", "test-integration", "test-e2e"})
    public StorageService localStorageAPI() {
        return new InMemoryStorageService();
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageService gcStorageAPI(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }
}
