package com.tresende.catalog.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tresende.catalog.application.genre.delete.DeleteGenreUseCase;
import com.tresende.catalog.application.genre.save.SaveGenreUseCase;
import com.tresende.catalog.infrastructure.category.models.GenreEvent;
import com.tresende.catalog.infrastructure.configuration.json.Json;
import com.tresende.catalog.infrastructure.genre.GenreGateway;
import com.tresende.catalog.infrastructure.kafka.models.connect.MessageValue;
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
public class GenreListener {

    private static final Logger LOG = LoggerFactory.getLogger(GenreListener.class.getName());

    private final SaveGenreUseCase saveGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final GenreGateway genreGateway;

    public GenreListener(final SaveGenreUseCase saveGenreUseCase, final DeleteGenreUseCase deleteGenreUseCase, final GenreGateway genreGateway) {
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @KafkaListener(
            concurrency = "${kafka.consumer.genres.concurrency}",
            topics = "${kafka.consumer.genres.topics}",
            groupId = "${kafka.consumer.genres.group-id}",
            id = "${kafka.consumer.genres.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumer.genres.auto-offset-reset}",
            }
    )
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "${kafka.consumer.genres.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("Received message [topic:{}] [partition:{}]  [offset:{}] {} ", metadata.topic(), metadata.partition(), metadata.offset(), null);
        final var typeReference = new TypeReference<MessageValue<GenreEvent>>() {
        };
        final var messagePayload = Json.readValue(payload, typeReference).payload();
        final var op = messagePayload.operation();
        if (payload.contains("teste")) {
            throw new RuntimeException("Teste exception");
        }

        if (op.isDelete()) {
            deleteGenreUseCase.execute(messagePayload.before().id());
        } else {
            genreGateway.genreOfId(messagePayload.before().id())
                    .map(it -> new SaveGenreUseCase.Input(
                            it.id(),
                            it.name(),
                            it.active(),
                            it.categories(),
                            it.createdAt(),
                            it.updatedAt(),
                            it.deletedAt()
                    ))
                    .ifPresentOrElse(saveGenreUseCase::execute, () -> {
                        LOG.warn("Genre not found for id: {}", messagePayload.before().id());
                    });
        }
    }

    @DltHandler
    public void onDLTMessage(@Payload final String message, final ConsumerRecordMetadata metadata) {
        System.out.println(message);
        LOG.warn("Received message in DLT [topic:{}] [partition:{}]  [offset:{}] {} ", metadata.topic(), metadata.partition(), metadata.offset(), message);
    }
}
