package com.tresende.catalog.admin.infrastructure.services.local;

import com.tresende.catalog.admin.domain.resource.Resource;
import com.tresende.catalog.admin.infrastructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {
    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public List<String> list(final String prefix) {
        if (prefix == null) return Collections.emptyList();
        return storage.keySet().stream().filter(it -> it.startsWith(prefix)).toList();
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        names.forEach(storage::remove);
    }

    @Override
    public void store(final String name, final Resource resource) {
        storage.put(name, resource);
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(storage.get(name));
    }

    public void clear() {
        storage.clear();
    }

    public Map<String, Resource> storage() {
        return storage;
    }
}
