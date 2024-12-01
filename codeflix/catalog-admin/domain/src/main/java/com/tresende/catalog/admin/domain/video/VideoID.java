package com.tresende.catalog.admin.domain.video;

import com.tresende.catalog.admin.domain.Identifier;
import com.tresende.catalog.admin.domain.utils.IdUtils;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {
    private String value;

    private VideoID(final String value) {
        this.value = Objects.requireNonNull(value, "'id' should not be null");
    }

    public static VideoID unique() {
        return VideoID.from(IdUtils.uuid());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId);
    }

    public static VideoID from(final UUID anId) {
        return new VideoID(anId.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoID that = (VideoID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}