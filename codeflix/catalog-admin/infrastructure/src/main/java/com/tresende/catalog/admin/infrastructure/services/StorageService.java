package com.tresende.catalog.admin.infrastructure.services;

import com.tresende.catalog.admin.domain.resource.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StorageService {
    List<String> list(String prefix);

    void deleteAll(Collection<String> names);

    void store(String name, Resource resource);

    Optional<Resource> get(String name);
}
