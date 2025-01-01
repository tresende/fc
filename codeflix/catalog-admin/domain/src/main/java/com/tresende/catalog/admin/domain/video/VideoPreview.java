package com.tresende.catalog.admin.domain.video;

import java.time.Instant;

public record VideoPreview(
        String id,
        String description,
        String title,
        Instant createdAt,
        Instant updatedAt
) {


    public VideoPreview(final Video aVideo) {
        this(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }
}
