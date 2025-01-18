package com.tresende.catalog.admin.infrastructure.video;

import com.tresende.catalog.admin.IntegrationTest;
import com.tresende.catalog.admin.domain.video.*;
import com.tresende.catalog.admin.infrastructure.services.StorageService;
import com.tresende.catalog.admin.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.tresende.catalog.admin.domain.Fixture.Videos.mediaType;
import static com.tresende.catalog.admin.domain.Fixture.Videos.resource;

@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private StorageService storageService;
    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(storageService);
        Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
        Assertions.assertNotNull(mediaResourceGateway);
        Assertions.assertInstanceOf(MediaResourceGateway.class, mediaResourceGateway);
    }

    @BeforeEach
    public void setup() {
        storageService().clear();
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        //given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        //when
        final var actualMedia = mediaResourceGateway.storeAudioVideo(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        //then
        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.rawLocation());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualMedia.status());
        Assertions.assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());
        final var actualStored = storageService().storage().get(expectedLocation);
        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        //given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        //when
        final var actualMedia = mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        //then
        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.location());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());
        final var actualStored = storageService().storage().get(expectedLocation);
        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsClearResource_shouldDeleteAll() {
        //given
        final var expectedVideo01Id = VideoID.unique();
        final var expectedVideo02Id = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(expectedVideo01Id.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(expectedVideo01Id.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(expectedVideo01Id.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(expectedVideo02Id.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(expectedVideo02Id.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(id -> storageService().storage().put(id, resource(mediaType())));
        expectedValues.forEach(id -> storageService().storage().put(id, resource(mediaType())));

        Assertions.assertEquals(5, storageService().storage().size());

        //when
        mediaResourceGateway.clearResources(expectedVideo01Id);
        final var acutalKeys = storageService().storage().keySet();

        //then
        Assertions.assertEquals(2, storageService().storage().size());
        Assertions.assertTrue(expectedValues.size() == storageService().storage().size() && acutalKeys.containsAll(expectedValues));
    }

}