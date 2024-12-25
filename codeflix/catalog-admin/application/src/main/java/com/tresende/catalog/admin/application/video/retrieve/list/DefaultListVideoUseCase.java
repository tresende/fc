package com.tresende.catalog.admin.application.video.retrieve.list;

import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.video.VideoGateway;
import com.tresende.catalog.admin.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideoUseCase extends ListVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultListVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery).map(VideoListOutput::from);
    }
}
