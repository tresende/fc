package com.tresende.catalog.infrastructure.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CategoryListener {


    @KafkaListener(
            id = "${kafka.consumer.categories.id}",
            topics = "${kafka.consumer.categories.topics}"
    )
    public void mock(Object data) {
        // Logic to handle category creation event
        System.out.println("Category created");
    }
}
