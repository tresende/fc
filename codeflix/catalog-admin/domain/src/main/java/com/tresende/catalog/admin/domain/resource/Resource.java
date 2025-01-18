package com.tresende.catalog.admin.domain.resource;

import com.tresende.catalog.admin.domain.ValueObject;

import java.util.Objects;

public class Resource extends ValueObject {
    private final String checksum;
    private final byte[] content;
    private final String contentType;
    private final String name;

    private Resource(final String checksum,
                     final byte[] content,
                     final String contentType,
                     final String name) {
        this.checksum = Objects.requireNonNull(checksum);
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
    }

    public static Resource of(final String checksum, final byte[] content, final String contentType, final String name) {
        return new Resource(checksum, content, contentType, name);
    }

    public byte[] content() {
        return content;
    }

    public String contentType() {
        return contentType;
    }

    public String name() {
        return name;
    }

    public String checksum() {
        return checksum;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Resource resource = (Resource) o;
        return Objects.equals(checksum, resource.checksum) && Objects.equals(contentType, resource.contentType) && Objects.equals(name, resource.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, contentType, name);
    }
}
