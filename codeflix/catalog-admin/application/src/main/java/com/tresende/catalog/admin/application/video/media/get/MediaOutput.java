package com.tresende.catalog.admin.application.video.media.get;

import com.tresende.catalog.admin.domain.resource.Resource;

public record MediaOutput(
        byte[] content,
        String contentType,
        String name
) {
    public static MediaOutput with(final Resource resource) {
        return new MediaOutput(resource.content(), resource.contentType(), resource.name());
    }
}
