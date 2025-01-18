package com.tresende.catalog.admin.infrastructure.services.local;

import com.tresende.catalog.admin.domain.Fixture;
import com.tresende.catalog.admin.domain.utils.IdUtils;
import com.tresende.catalog.admin.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InMemoryStorageServiceTest {

    private final InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setup() {
        target.clear();
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        // Given
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = IdUtils.uuid();

        // When
        target.store(expectedName, expectedResource);

        // Then
        Assertions.assertEquals(expectedResource, target.storage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldRetrieveIt() {
        // Given
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = IdUtils.uuid();
        target.storage().put(expectedName, expectedResource);

        // When
        final var actualResource = target.get(expectedName).get();

        // Then
        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldBeEmpty() {
        // Given
        final var expectedName = IdUtils.uuid();

        // When
        final var actualResource = target.get(expectedName);

        // Then
        Assertions.assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_shouldRetrieveAll() {
        // Given
        final var expectedItemsCount = 3;
        final var expectedNames = List.of(
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid()
        );
        final var all = new ArrayList<>(expectedNames);
        all.add("image_" + IdUtils.uuid());
        all.add("image_" + IdUtils.uuid());

        all.forEach(it -> target.storage().put(it, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        Assertions.assertEquals(5, target.storage().size());

        // When
        final var actualResource = target.list("video_");

        // Then
        Assertions.assertEquals(expectedItemsCount, actualResource.size());
    }

    @Test
    public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
        // Given
        final var expectedItemsCount = 2;

        final var expectedNames = Set.of(
                "image_" + IdUtils.uuid(),
                "image_" + IdUtils.uuid()
        );

        final var videos = List.of(
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid()
        );

        final var all = new ArrayList<>(expectedNames);
        all.addAll(videos);

        all.forEach(it -> target.storage().put(it, Fixture.Videos.resource(VideoMediaType.VIDEO)));

        Assertions.assertEquals(5, target.storage().size());

        // When
        target.deleteAll(videos);

        // Then
        Assertions.assertEquals(expectedItemsCount, target.storage().size());
        Assertions.assertEquals(expectedNames, target.storage().keySet());
    }
}