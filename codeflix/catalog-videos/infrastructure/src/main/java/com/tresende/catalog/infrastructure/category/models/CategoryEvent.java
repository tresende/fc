package com.tresende.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryEvent(
        @JsonProperty("id") String id
) {
}