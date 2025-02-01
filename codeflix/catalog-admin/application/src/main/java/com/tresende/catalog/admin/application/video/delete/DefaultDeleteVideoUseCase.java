package com.tresende.catalog.admin.application.video.delete;

import com.tresende.catalog.admin.domain.video.MediaResourceGateway;
import com.tresende.catalog.admin.domain.video.VideoGateway;
import com.tresende.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String anIn) {
        videoGateway.deleteById(VideoID.from(anIn));
        mediaResourceGateway.clearResources(VideoID.from(anIn));
    }
}
