package com.tresende.catalog.admin.infrastructure.amqp;

import com.tresende.catalog.admin.AmqpTest;
import com.tresende.catalog.admin.application.video.media.update.UpdateMediaStatusCommand;
import com.tresende.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.tresende.catalog.admin.domain.utils.IdUtils;
import com.tresende.catalog.admin.domain.video.MediaStatus;
import com.tresende.catalog.admin.infrastructure.configuration.annotations.VideoEncodedQueue;
import com.tresende.catalog.admin.infrastructure.configuration.json.Json;
import com.tresende.catalog.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import com.tresende.catalog.admin.infrastructure.video.models.VideoEncoderCompleted;
import com.tresende.catalog.admin.infrastructure.video.models.VideoEncoderError;
import com.tresende.catalog.admin.infrastructure.video.models.VideoMessage;
import com.tresende.catalog.admin.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static com.tresende.catalog.admin.infrastructure.amqp.VideoEncoderListener.LISTENER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@AmqpTest
public class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    public void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
        //given
        final var expectedError = new VideoEncoderError(
                new VideoMessage("123", "abc"),
                "Video not found"
        );
        final var expectedMessage = Json.writeValueAsString(expectedError);

        //when
        rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        //then
        final var invocationDataFor = harness.getNextInvocationDataFor(LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationDataFor);
        Assertions.assertNotNull(invocationDataFor.getArguments());

        final var actualMessage = (String) invocationDataFor.getArguments()[0];
        Assertions.assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void givenCompletedResult_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        //given
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata =
                new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);
        final var expectedMessage = Json.writeValueAsString(aResult);
        doNothing().when(updateMediaStatusUseCase).execute(any());

        //when
        rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);


        //then
        final var invocationDataFor = harness.getNextInvocationDataFor(LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationDataFor);
        Assertions.assertNotNull(invocationDataFor.getArguments());

        final var actualMessage = (String) invocationDataFor.getArguments()[0];
        Assertions.assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);

        verify(updateMediaStatusUseCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();
        Assertions.assertEquals(expectedId, actualCommand.videoId());
        Assertions.assertEquals(expectedStatus, actualCommand.status());
        Assertions.assertEquals(expectedResourceId, actualCommand.resourceId());
        Assertions.assertEquals(expectedFilePath, actualCommand.filename());
        Assertions.assertEquals(expectedEncoderVideoFolder, actualCommand.folder());

    }

}