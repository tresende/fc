package com.tresende.catalog.admin.application.video.upadate;

import com.tresende.catalog.admin.domain.video.Video;

public record UpdateVideoOutput(
        String id
) {
    public static UpdateVideoOutput from(final Video aVideo) {
        return new UpdateVideoOutput(aVideo.getId().getValue());
    }
}
