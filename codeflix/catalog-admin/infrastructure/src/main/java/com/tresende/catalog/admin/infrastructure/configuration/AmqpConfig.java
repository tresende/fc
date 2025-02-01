package com.tresende.catalog.admin.infrastructure.configuration;

import com.tresende.catalog.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    public QueueProperties videoCreatedQueue() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    public QueueProperties videoEncodedQueue() {
        return new QueueProperties();
    }
}
