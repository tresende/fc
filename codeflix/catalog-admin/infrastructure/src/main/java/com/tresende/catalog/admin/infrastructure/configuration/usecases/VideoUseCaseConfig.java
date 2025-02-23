package com.tresende.catalog.admin.infrastructure.configuration.usecases;

import com.tresende.catalog.admin.application.video.create.CreateVideoUseCase;
import com.tresende.catalog.admin.application.video.create.DefaultCreateVideoUseCase;
import com.tresende.catalog.admin.application.video.delete.DefaultDeleteVideoUseCase;
import com.tresende.catalog.admin.application.video.delete.DeleteVideoUseCase;
import com.tresende.catalog.admin.application.video.media.get.DefaultGetMediaUseCase;
import com.tresende.catalog.admin.application.video.media.get.GetMediaUseCase;
import com.tresende.catalog.admin.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.tresende.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.tresende.catalog.admin.application.video.media.upload.DefaultUploadMediaUseCase;
import com.tresende.catalog.admin.application.video.media.upload.UploadMediaUseCase;
import com.tresende.catalog.admin.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.tresende.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.tresende.catalog.admin.application.video.retrieve.list.DefaultListVideoUseCase;
import com.tresende.catalog.admin.application.video.retrieve.list.ListVideoUseCase;
import com.tresende.catalog.admin.application.video.upadate.DefaultUpdateVideoUseCase;
import com.tresende.catalog.admin.application.video.upadate.UpdateVideoUseCase;
import com.tresende.catalog.admin.domain.castmember.CastMemberGateway;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.video.MediaResourceGateway;
import com.tresende.catalog.admin.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway,
            final VideoGateway videoGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(
                categoryGateway,
                castMemberGateway,
                genreGateway,
                mediaResourceGateway,
                videoGateway
        );
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideoUseCase listVideosUseCase() {
        return new DefaultListVideoUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
