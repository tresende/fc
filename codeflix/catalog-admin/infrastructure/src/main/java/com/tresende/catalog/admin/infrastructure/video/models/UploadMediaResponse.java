package com.tresende.catalog.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tresende.catalog.admin.domain.video.VideoMediaType;

public record UploadMediaResponse(
        @JsonProperty("video_id") String videoId,
        @JsonProperty("media_type") VideoMediaType mediaType
) {
}