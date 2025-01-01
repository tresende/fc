package com.tresende.catalog.admin.application.video.retrieve.list;

import com.tresende.catalog.admin.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(
        String id,
        String title,
        String description,
        //Set<String> categories,
        Instant createdAt,
        Instant updatedAt
) {

    public static VideoListOutput from(final VideoPreview aVideo) {
        return new VideoListOutput(
                aVideo.id(),
                aVideo.title(),
                aVideo.description(),
                //aVideo.getCategories().stream().map(CategoryID::getValue).collect(Collectors.toSet()),
                aVideo.createdAt(),
                aVideo.updatedAt()
        );
    }
}
