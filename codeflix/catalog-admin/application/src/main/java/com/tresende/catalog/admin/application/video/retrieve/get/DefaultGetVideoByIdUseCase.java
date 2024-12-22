package com.tresende.catalog.admin.application.video.retrieve.get;

import com.tresende.catalog.admin.domain.exceptions.NotFoundException;
import com.tresende.catalog.admin.domain.video.Video;
import com.tresende.catalog.admin.domain.video.VideoGateway;
import com.tresende.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anId) {
        final var videoId = VideoID.from(anId);
        return videoGateway.findById(videoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, videoId));
    }
}
