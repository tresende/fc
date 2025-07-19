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
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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
    public void testCategoriesTopic() throws ExecutionException, InterruptedException {
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.categories";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.categories-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.categories-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.categories-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.categories-dlt";

        final var topics = admin().listTopics().listings().get().stream()
                .map(TopicListing::name)
                .collect(Collectors.toSet());

        Assertions.assertTrue(topics.contains(expectedMainTopic));
        Assertions.assertTrue(topics.contains(expectedRetry0Topic));
        Assertions.assertTrue(topics.contains(expectedRetry1Topic));
        Assertions.assertTrue(topics.contains(expectedRetry2Topic));
        Assertions.assertTrue(topics.contains(expectedDLTTopic));
    }

    @Test
    public void givenUpdateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());
        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, aulasEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(saveCategoryUseCase).execute(any());

        doReturn(Optional.of(aulas)).when(categoryGateway).categoryOfId(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryGateway, times(1)).categoryOfId(eq(aulas.id()));
        verify(saveCategoryUseCase, times(1)).execute(eq(aulas));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());
        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, aulasEvent, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(saveCategoryUseCase).execute(any());


        doReturn(Optional.of(aulas)).when(categoryGateway).categoryOfId(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryGateway, times(1)).categoryOfId(eq(aulas.id()));
        verify(saveCategoryUseCase, times(1)).execute(eq(aulas));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());
        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, aulasEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(deleteCategoryUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(deleteCategoryUseCase, times(1)).execute(eq(aulas.id()));
    }

    @Test
    public void testRetriesAndDLT() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        final var expectedMaxAttempts = 4;
        final var expectedMaxDLTAttempts = 1;
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.categories";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.categories-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.categories-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.categories-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.categories-dlt";

        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());

        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, aulasEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(4);

        doAnswer(t -> {
            latch.countDown();
            throw new RuntimeException("BOOM!");
        }).when(deleteCategoryUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryListener, times(expectedMaxAttempts)).onMessage(eq(message), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        //verify with timeout
        verify(categoryListener, timeout(1000).times(expectedMaxDLTAttempts)).onDLTMessage(eq(message), metadata.capture());
//        verify(categoryListener, times(expectedMaxDLTAttempts)).onDLTMessage(eq(message), metadata.capture());
        Assertions.assertEquals(expectedDLTTopic, metadata.getValue().topic());
    }
}
