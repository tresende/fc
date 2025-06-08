package com.tresende.catalog.infrastructure.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private Long poolTimeout;
    private String autoCreateTopics;

    public Long poolTimeout() {
        return poolTimeout;
    }

    public void setPoolTimeout(final Long poolTimeout) {
        this.poolTimeout = poolTimeout;
    }

    public String bootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(final String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String autoCreateTopics() {
        return autoCreateTopics;
    }

    public void setAutoCreateTopics(final String autoCreateTopics) {
        this.autoCreateTopics = autoCreateTopics;
    }
}
