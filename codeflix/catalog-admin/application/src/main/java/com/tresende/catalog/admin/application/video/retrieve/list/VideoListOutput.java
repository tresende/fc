package com.tresende.catalog.admin.application.video.retrieve.list;

import com.tresende.catalog.admin.domain.video.Video;

import java.time.Instant;

public record VideoListOutput(
        String id,
        String title,
        String description,
        //Set<String> categories,
        Instant createdAt,
        Instant updatedAt
) {

    public static VideoListOutput from(final Video aVideo) {
        return new VideoListOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                //aVideo.getCategories().stream().map(CategoryID::getValue).collect(Collectors.toSet()),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }
}
