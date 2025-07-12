package com.tresende.catalog.infrastructure.kafka;

import com.tresende.catalog.AbstractEmbeddedKafkaTest;
import com.tresende.catalog.application.category.delete.DeleteCategoryUseCase;
import com.tresende.catalog.application.category.save.SaveCategoryUseCase;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.infrastructure.category.CategoryGateway;
import com.tresende.catalog.infrastructure.category.models.CategoryEvent;
import com.tresende.catalog.infrastructure.configuration.json.Json;
import com.tresende.catalog.infrastructure.kafka.models.connect.MessageValue;
import com.tresende.catalog.infrastructure.kafka.models.connect.Operation;
import com.tresende.catalog.infrastructure.kafka.models.connect.ValuePayload;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class CategoryListenerTest extends AbstractEmbeddedKafkaTest {

    @MockitoBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private CategoryGateway categoryGateway;

    @MockitoSpyBean
    private CategoryListener categoryListener;

    @Value("${kafka.consumer.categories.topics}")
    private String categoryTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testDummy() {
        Assertions.assertNotNull(producer());
    }

    @Test
    public void testRetriesAndDLT() throws InterruptedException {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());
        final var messageValue = new MessageValue<>(
                new ValuePayload<>(
                        aulasEvent,
                        aulasEvent,
                        aSource(),
                        Operation.DELETE
                )
        );
        final var message = Json.writeValueAsString(messageValue);

        final var latch = new CountDownLatch(5);
        doAnswer(t -> new RuntimeException("Simulated error"))
                .when(deleteCategoryUseCase)
                .execute(Mockito.any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message));
        producer().flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryListener, times(4)).onMessage(anyString(), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories", allMetas.get(0).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-0", allMetas.get(1).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-1", allMetas.get(2).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-2", allMetas.get(3).topic());

        // then
//        verify(categoryListener, times(4)).onDltMessage(anyString(), metadata.capture());
    }
}
