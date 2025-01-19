package com.tresende.catalog.admin.application.video.media.get;

import com.tresende.catalog.admin.domain.exceptions.NotFoundException;
import com.tresende.catalog.admin.domain.validation.Error;
import com.tresende.catalog.admin.domain.video.MediaResourceGateway;
import com.tresende.catalog.admin.domain.video.VideoID;
import com.tresende.catalog.admin.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(final GetMediaCommand aCommand) {
        final var aType = VideoMediaType.of(aCommand.mediaType())
                .orElseThrow(() -> typeNotFound(aCommand.mediaType()));
        final var videoId = VideoID.from(aCommand.videoId());

        final var resource = mediaResourceGateway.getResource(videoId, aType)
                .orElseThrow(() -> notFound(videoId.getValue(), aType.name()));

        return MediaOutput.with(resource);
    }

    private NotFoundException notFound(final String anId, final String aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
    }

    private NotFoundException typeNotFound(final String aType) {
        return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(aType)));
    }
}
