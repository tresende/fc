package com.tresende.catalog.admin.application.video.create;

import com.tresende.catalog.admin.domain.video.Video;
import com.tresende.catalog.admin.domain.video.VideoID;

public record CreateVideoOutput(
        String id
) {

    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }

    public static CreateVideoOutput from(final VideoID anId) {
        return new CreateVideoOutput(anId.getValue());
    }
}
