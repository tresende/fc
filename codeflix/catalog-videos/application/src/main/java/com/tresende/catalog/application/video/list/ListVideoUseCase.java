package com.tresende.catalog.application.video.list;


import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.domain.video.Video;
import com.tresende.catalog.domain.video.VideoGateway;
import com.tresende.catalog.domain.video.VideoSearchQuery;

import java.util.Objects;
import java.util.Set;

public class ListVideoUseCase extends UseCase<ListVideoUseCase.Input, Pagination<ListVideoUseCase.Output>> {

    private final VideoGateway videoGateway;

    public ListVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<Output> execute(final ListVideoUseCase.Input input) {
        final var aQuery = new VideoSearchQuery(
                input.page(),
                input.perPage(),
                input.terms(),
                input.sort(),
                input.direction(),
                input.rating(),
                input.year(),
                input.categories(),
                input.castMembers(),
                input.genres()
        );

        return this.videoGateway.findAll(aQuery)
                .map(Output::from);
    }

    public record Input(
            int page,
            int perPage,
            String terms,
            String sort,
            String direction,
            Integer year,
            String rating,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
    ) {

    }

    public record Output(
            String id,
            String title,
            boolean published,
            int launchedYear,
            String rating,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
            ) {

        public static Output from(Video video) {
            return new Output(
                    video.id(),
                    video.title(),
                    video.published(),
                    video.launchedAt().getValue(),
                    video.rating().getName(),
                    video.categories(),
                    video.castMembers(),
                    video.genres()
            );
        }
    }
}
