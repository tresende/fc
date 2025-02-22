package com.tresende.catalog.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoResponse(
        @JsonProperty("id") String id
) {
}
