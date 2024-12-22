package com.tresende.catalog.admin.application.video.delete;

import com.tresende.catalog.admin.domain.video.VideoGateway;
import com.tresende.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final String anIn) {
        videoGateway.deleteById(VideoID.from(anIn));
    }
}
