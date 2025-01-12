package com.tresende.catalog.admin.infrastructure.services.impl;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.tresende.catalog.admin.domain.resource.Resource;
import com.tresende.catalog.admin.infrastructure.services.StorageService;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class GCStorageService implements StorageService {

    private final String bucket;
    private final Storage storage;

    public GCStorageService(final String bucket, final Storage storage) {
        this.bucket = Objects.requireNonNull(bucket);
        this.storage = Objects.requireNonNull(storage);
    }

    @Override
    public List<String> list(final String prefix) {
        final var blobs = storage.list(this.bucket, Storage.BlobListOption.prefix(prefix));
        return StreamSupport.stream(blobs.iterateAll().spliterator(), false)
                .map(BlobInfo::getBlobId)
                .map(BlobId::getName)
                .toList();
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        final var blobs = names.stream()
                .map(it -> BlobId.of(bucket, bucket))
                .toList();

        storage.delete(blobs);
    }

    @Override
    public void store(final String name, final Resource resource) {
        final var blobInfo = BlobInfo.newBuilder(bucket, name)
                .setContentType(resource.contentType())
                .setCrc32cFromHexString(resource.checksum())
                .build();
        storage.create(blobInfo, resource.content());
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(storage.get(bucket, name)).map(it -> Resource.of(
                it.getCrc32cToHexString(),
                it.getContent(),
                name,
                it.getContentType()
        ));
    }
}
