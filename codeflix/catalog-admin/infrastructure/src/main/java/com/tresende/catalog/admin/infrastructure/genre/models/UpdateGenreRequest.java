package com.tresende.catalog.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateGenreRequest(
        @JsonProperty("name") String name,
        @JsonProperty("categories") List<String> categories,
        @JsonProperty("is_active") Boolean active
) {
}
