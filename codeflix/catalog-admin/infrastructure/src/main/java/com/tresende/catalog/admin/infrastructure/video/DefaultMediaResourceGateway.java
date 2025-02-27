package com.tresende.catalog.admin.infrastructure.video;

import com.tresende.catalog.admin.domain.resource.Resource;
import com.tresende.catalog.admin.domain.video.*;
import com.tresende.catalog.admin.infrastructure.configuration.properties.storage.StorageProperties;
import com.tresende.catalog.admin.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(
            final StorageProperties storageProperties,
            final StorageService storageService
    ) {
        this.filenamePattern = storageProperties.getFileNamePattern();
        this.locationPattern = storageProperties.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filepath = filepath(anId, videoResource.type());
        final var aResource = videoResource.resource();
        store(filepath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource imageResource) {
        final var filepath = filepath(anId, imageResource.type());
        final var aResource = imageResource.resource();
        store(filepath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = storageService.list(folder(anId));
        storageService.deleteAll(ids);
    }

    @Override
    public Optional<Resource> getResource(final VideoID anId, final VideoMediaType aType) {
        return storageService.get(filepath(anId, aType));
    }

    private String filepath(final VideoID anId, final VideoMediaType aType) {
        return folder(anId).concat("/").concat(filename(aType));
    }

    private String filename(final VideoMediaType aType) {
        return filenamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private void store(final String filepath, final Resource aResource) {
        storageService.store(filepath, aResource);
    }
}
