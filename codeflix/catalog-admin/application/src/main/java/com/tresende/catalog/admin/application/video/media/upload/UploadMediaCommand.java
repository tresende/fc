package com.tresende.catalog.admin.application.video.media.upload;

import com.tresende.catalog.admin.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {
    public static UploadMediaCommand with(final String anId, final VideoResource aResource) {
        return new UploadMediaCommand(anId, aResource);
    }
}