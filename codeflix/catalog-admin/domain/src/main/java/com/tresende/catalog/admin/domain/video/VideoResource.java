package com.tresende.catalog.admin.domain.video;

import com.tresende.catalog.admin.domain.ValueObject;
import com.tresende.catalog.admin.domain.resource.Resource;

import java.util.Objects;

public class VideoResource extends ValueObject {
    private final VideoMediaType type;
    private final Resource resource;

    private VideoResource(final Resource aResource, final VideoMediaType aType) {
        this.resource = Objects.requireNonNull(aResource);
        this.type = Objects.requireNonNull(aType);
    }

    public static VideoResource with(final VideoMediaType aType, final Resource aResource) {
        return new VideoResource(aResource, aType);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final VideoResource that = (VideoResource) o;
        return type() == that.type();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type());
    }

    public VideoMediaType type() {
        return type;
    }

    public Resource getResource() {
        return resource;
    }
}
