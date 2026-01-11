package com.tresende.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenreEvent(
        @JsonProperty("id") String id
) {
}