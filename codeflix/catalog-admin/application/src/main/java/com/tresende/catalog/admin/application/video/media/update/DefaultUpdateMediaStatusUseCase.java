package com.tresende.catalog.admin.application.video.media.update;

import com.tresende.catalog.admin.domain.exceptions.NotFoundException;
import com.tresende.catalog.admin.domain.video.*;

import java.util.Objects;


public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {

    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aResourceId = aCmd.resourceId();
        final var folder = aCmd.folder();
        final var filename = aCmd.filename();

        final var aVideo = videoGateway.findById(anId)
                .orElseThrow(() -> notFound(anId));
        final var encodedPath = "%s/%s".formatted(folder, filename);

        if (matches(aResourceId, aVideo.getVideo().orElse(null))) {
            updateVideo(VideoMediaType.VIDEO, aCmd.status(), aVideo, encodedPath);
            videoGateway.update(aVideo);
        } else if (matches(aResourceId, aVideo.getTrailer().orElse(null))) {
            updateVideo(VideoMediaType.TRAILER, aCmd.status(), aVideo, encodedPath);
            videoGateway.update(aVideo);
        }
    }

    private boolean matches(final String aResourceId, final AudioVideoMedia media) {
        return media != null && media.id().equals(aResourceId);
    }

    private NotFoundException notFound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }


    private void updateVideo(final VideoMediaType aType, final MediaStatus aStatus, final Video aVideo, final String encodedPath) {
        switch (aStatus) {
            case PROCESSING -> aVideo.processing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);
            case PENDING -> {
            }
        }
    }
}
