package com.tresende.catalog.admin.infrastructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.tresende.catalog.admin.domain.Fixture;
import com.tresende.catalog.admin.domain.resource.Resource;
import com.tresende.catalog.admin.domain.utils.IdUtils;
import com.tresende.catalog.admin.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.google.cloud.storage.Storage.BlobListOption.prefix;
import static org.mockito.Mockito.*;

class GCStorageServiceTest {

    private GCStorageService target;
    private Storage storage;
    private String bucket = "fc3_test";

    @BeforeEach
    public void setup() {
        this.storage = mock(Storage.class);
        this.target = new GCStorageService(bucket, storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldPersisIt() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

        //when
        target.store(expectedName, expectedResource);

        //then
        final var captor = ArgumentCaptor.forClass(BlobInfo.class);
        verify(storage, times(1)).create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();
        Assertions.assertEquals(bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedName, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedName, actualBlob.getName());
        Assertions.assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldRetrieveIt() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).get(anyString(), anyString());

        //when
        final var actualResource = target.get(expectedName).get();

        //then
        verify(storage, times(1)).get(eq(bucket), eq(expectedName));

        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldBeEmpty() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        doReturn(null).when(storage).get(anyString(), anyString());

        //when
        final var actualResource = target.get(expectedName);

        //then
        verify(storage, times(1)).get(eq(bucket), eq(expectedName));

        Assertions.assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_shouldRetrieveAll() {
        final var expectedPrefix = "media_";
        final var expectedVideoName = expectedPrefix + IdUtils.uuid();
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blobVideo = mockBlob(expectedVideoName, expectedVideo);

        final var expectedBannerName = expectedPrefix + IdUtils.uuid();
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var blobBanner = mockBlob(expectedBannerName, expectedBanner);

        final var expectedBlobs = List.of(blobVideo, blobBanner);
        final var expectedResources = List.of(expectedVideoName, expectedBannerName);

        final var page = mock(Page.class);
        doReturn(expectedBlobs).when(page).iterateAll();
        doReturn(page).when(storage).list(anyString(), any());

        //when
        final var actualResources = target.list(expectedPrefix);

        //then
        verify(storage, times(1)).list(eq(bucket), eq(prefix(expectedPrefix)));
        Assertions.assertTrue(
                expectedResources.size() == actualResources.size()
                        && expectedResources.containsAll(actualResources)
        );

    }

    @Test
    public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
        final var expectedPrefix = "media_";
        final var expectedVideoName = expectedPrefix + IdUtils.uuid();
        final var expectedBannerName = expectedPrefix + IdUtils.uuid();
        final var expectedResources = List.of(expectedVideoName, expectedBannerName);

        //when
        target.deleteAll(expectedResources);

        //then
        final var captor = ArgumentCaptor.forClass(List.class);
        verify(storage, times(1)).delete(captor.capture());
        final var actualResources = ((List<BlobId>) captor.getValue()).stream().map(BlobId::getName).toList();

        Assertions.assertTrue(
                expectedResources.size() == actualResources.size()
                        && expectedResources.containsAll(actualResources)
        );
    }

    private Blob mockBlob(final String aName, final Resource aResource) {
        final var blob = mock(Blob.class);
        when(blob.getBlobId()).thenReturn(BlobId.of(bucket, aName));
        when(blob.getCrc32cToHexString()).thenReturn(aResource.checksum());
        when(blob.getContent()).thenReturn(aResource.content());
        when(blob.getContentType()).thenReturn(aResource.contentType());
        when(blob.getName()).thenReturn(aResource.name());
        return blob;
    }
}