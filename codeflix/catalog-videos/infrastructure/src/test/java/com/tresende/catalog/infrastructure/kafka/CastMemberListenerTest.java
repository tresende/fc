package com.tresende.catalog.infrastructure.kafka;

import com.tresende.catalog.AbstractEmbeddedKafkaTest;
import com.tresende.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.tresende.catalog.application.castmember.save.SaveCastMemberUseCase;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.infrastructure.castmember.models.CastMemberEvent;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CastMemberListenerTest extends AbstractEmbeddedKafkaTest {

    @MockitoBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @MockitoBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockitoSpyBean
    private CastMemberListener castMemberListener;

    @Value("${kafka.consumer.cast-members.topics}")
    private String castMemberTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testDummy() {
        Assertions.assertNotNull(producer());
    }

    @Test
    public void testCastMembersTopic() throws ExecutionException, InterruptedException {
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.cast_members";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.cast_members-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.cast_members-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.cast_members-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.cast_members-dlt";

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
        final var gabriel = Fixture.CastMembers.gabriel();
        final var gabrielEvent = CastMemberEvent.from(gabriel);
        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(gabrielEvent, gabrielEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(saveCastMemberUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(castMemberTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(saveCastMemberUseCase, times(1)).execute(eq(gabriel));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();
        final var gabrielEvent = CastMemberEvent.from(gabriel);
        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(gabrielEvent, gabrielEvent, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(saveCastMemberUseCase).execute(any());


//        doReturn(Optional.of(aulas)).when(castMemberGateway).castMemberOfId(any());

        // when
        producer().send(new ProducerRecord<>(castMemberTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
//        verify(castMemberGateway, times(1)).castMemberOfId(eq(aulas.id()));
        verify(saveCastMemberUseCase, times(1)).execute(eq(gabriel));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();
        final var gabrielEvent = CastMemberEvent.from(gabriel);
        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(gabrielEvent, gabrielEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(deleteCastMemberUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(castMemberTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(deleteCastMemberUseCase, times(1)).execute(eq(gabriel.id()));
    }

    @Test
    public void testRetriesAndDLT() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        final var expectedMaxAttempts = 4;
        final var expectedMaxDLTAttempts = 1;
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.cast_members";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.cast_members-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.cast_members-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.cast_members-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.cast_members-dlt";

        final var gabriel = Fixture.CastMembers.gabriel();
        final var gabrielEvent = CastMemberEvent.from(gabriel);
        final var message = Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(gabrielEvent, gabrielEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(4);

        doAnswer(t -> {
            latch.countDown();
            throw new RuntimeException("BOOM!");
        }).when(deleteCastMemberUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(castMemberTopic, message)).get(10, TimeUnit.SECONDS);
        producer.flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(castMemberListener, times(expectedMaxAttempts)).onMessage(eq(message), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        //verify with timeout
        verify(castMemberListener, timeout(1000).times(expectedMaxDLTAttempts)).onDLTMessage(eq(message), metadata.capture());
//        verify(castMemberListener, times(expectedMaxDLTAttempts)).onDLTMessage(eq(message), metadata.capture());
        Assertions.assertEquals(expectedDLTTopic, metadata.getValue().topic());
    }
}
