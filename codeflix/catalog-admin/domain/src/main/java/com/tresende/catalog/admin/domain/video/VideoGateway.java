package com.tresende.catalog.admin.domain.video;

import com.tresende.catalog.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    void deleteById(VideoID anId);

    Optional<Video> findById(VideoID anId);

    Video update(Video aVideo);

    Pagination<Video> findAll(VideoSearchQuery aQuery);

}
