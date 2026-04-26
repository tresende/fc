package com.tresende.catalog.domain.video;

import com.tresende.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video save(Video aVideo);

    void deleteById(String anId);

    Optional<Video> findById(String anId);

    Pagination<Video> findAll(VideoSearchQuery aQuery);
}

