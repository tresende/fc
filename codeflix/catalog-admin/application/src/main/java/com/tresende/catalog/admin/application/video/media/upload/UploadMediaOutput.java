package com.tresende.catalog.admin.application.video.media.upload;

import com.tresende.catalog.admin.domain.video.Video;
import com.tresende.catalog.admin.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {
    public static UploadMediaOutput with(final Video aVideo, final VideoMediaType aMediaType) {
        return new UploadMediaOutput(aVideo.getId().getValue(), aMediaType);
    }
}
