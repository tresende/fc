package com.tresende.catalog.admin.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.tresende.catalog.admin.infrastructure.configuration.properties.GoogleStorageProperties;
import com.tresende.catalog.admin.infrastructure.services.StorageService;
import com.tresende.catalog.admin.infrastructure.services.impl.GCStorageService;
import com.tresende.catalog.admin.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean(name = "storageService")
    @Profile({"development", "production"})
    public StorageService gcStorageService(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}