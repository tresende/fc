package com.tresende.catalog.infrastructure.kafka;

import com.tresende.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tresende.catalog.application.category.save.SaveCategoryUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CategoryListener {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryListener.class.getName());

    private final SaveCategoryUseCase saveCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryListener(final SaveCategoryUseCase saveCategoryUseCase, final DeleteCategoryUseCase deleteCategoryUseCase) {
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumer.categories.concurrency}",
            topics = "${kafka.consumer.categories.topics}",
            containerFactory = "containerFactory",
            groupId = "${kafka.consumer.categories.group-id}",
            id = "${kafka.consumer.categories.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumer.categories.auto-offset-reset}",
            }
    )
//    @RetryableTopic(
//            attempts = "4",
//            backoff = @Backoff(delay = 1000, multiplier = 2.0),
//            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
//            include = Exception.class,
//            exclude = {IllegalArgumentException.class, IllegalStateException.class}
//    )
    public void onMessage(@Payload final String message, final ConsumerRecordMetadata metadata) {
        LOG.info("Received message [topic:{}] [partition:{}]  [offset:{}] {} ", metadata.topic(), metadata.partition(), metadata.offset(), message);
    }


}
