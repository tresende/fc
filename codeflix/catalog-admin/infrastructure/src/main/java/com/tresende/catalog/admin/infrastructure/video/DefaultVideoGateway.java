package com.tresende.catalog.admin.infrastructure.video;

import com.tresende.catalog.admin.domain.Identifier;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.video.*;
import com.tresende.catalog.admin.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.tresende.catalog.admin.infrastructure.services.EventService;
import com.tresende.catalog.admin.infrastructure.utils.SqlUtils;
import com.tresende.catalog.admin.infrastructure.video.persistence.VideoJpaEntity;
import com.tresende.catalog.admin.infrastructure.video.persistence.VideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.tresende.catalog.admin.domain.utils.CollectionUtils.mapTo;
import static com.tresende.catalog.admin.domain.utils.CollectionUtils.nullIfEmpty;

@Component
class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;
    private final EventService eventService;

    public DefaultVideoGateway(final VideoRepository videoRepository, @VideoCreatedQueue final EventService eventService) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.eventService = Objects.requireNonNull(eventService);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var id = anId.getValue();
        if (videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public Optional<Video> findById(final VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(SqlUtils.upper(aQuery.terms())),
                nullIfEmpty(mapTo(aQuery.castMembers(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.genres(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(final Video aVideo) {
        final var entity = VideoJpaEntity.from(aVideo);
        final var result = this.videoRepository.save(entity).toAggregate();
        aVideo.publishDomainEvents(eventService::send);
        return result;
    }
}
