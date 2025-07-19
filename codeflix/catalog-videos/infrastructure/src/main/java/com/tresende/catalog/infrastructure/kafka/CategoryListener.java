package com.tresende.catalog.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tresende.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tresende.catalog.application.category.save.SaveCategoryUseCase;
import com.tresende.catalog.infrastructure.category.CategoryGateway;
import com.tresende.catalog.infrastructure.category.models.CategoryEvent;
import com.tresende.catalog.infrastructure.configuration.json.Json;
import com.tresende.catalog.infrastructure.kafka.models.connect.MessageValue;
import com.tresende.catalog.infrastructure.kafka.models.connect.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CategoryListener {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryListener.class.getName());

    private final SaveCategoryUseCase saveCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final CategoryGateway categoryGateway;

    public CategoryListener(final SaveCategoryUseCase saveCategoryUseCase, final DeleteCategoryUseCase deleteCategoryUseCase, final CategoryGateway categoryGateway) {
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @KafkaListener(
            concurrency = "${kafka.consumer.categories.concurrency}",
            topics = "${kafka.consumer.categories.topics}",
            groupId = "${kafka.consumer.categories.group-id}",
            id = "${kafka.consumer.categories.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumer.categories.auto-offset-reset}",
            }
    )
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "${kafka.consumer.categories.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("Received message [topic:{}] [partition:{}]  [offset:{}] {} ", metadata.topic(), metadata.partition(), metadata.offset(), null);
        final var typeReference = new TypeReference<MessageValue<CategoryEvent>>() {
        };
        final var messagePayload = Json.readValue(payload, typeReference).payload();
        final var op = messagePayload.operation();
        if (payload.contains("teste")) {
            throw new RuntimeException("Teste exception");
        }

        if (Operation.isDelete(op)) {
            deleteCategoryUseCase.execute(messagePayload.before().id());
        } else {
            categoryGateway.categoryOfId(messagePayload.before().id())
                    .ifPresentOrElse(saveCategoryUseCase::execute, () -> {
                        LOG.warn("Category not found for id: {}", messagePayload.before().id());
                    });
        }
    }

    @DltHandler
    public void onDLTMessage(@Payload final String message, final ConsumerRecordMetadata metadata) {

        System.out.println(message);
        LOG.warn("Received message in DLT [topic:{}] [partition:{}]  [offset:{}] {} ", metadata.topic(), metadata.partition(), metadata.offset(), message);
    }
}
