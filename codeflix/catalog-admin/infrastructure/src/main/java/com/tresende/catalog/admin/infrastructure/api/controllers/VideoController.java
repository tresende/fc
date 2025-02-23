package com.tresende.catalog.admin.infrastructure.api.controllers;

import com.tresende.catalog.admin.application.video.create.CreateVideoCommand;
import com.tresende.catalog.admin.application.video.create.CreateVideoUseCase;
import com.tresende.catalog.admin.application.video.delete.DeleteVideoUseCase;
import com.tresende.catalog.admin.application.video.media.get.GetMediaCommand;
import com.tresende.catalog.admin.application.video.media.get.GetMediaUseCase;
import com.tresende.catalog.admin.application.video.media.upload.UploadMediaCommand;
import com.tresende.catalog.admin.application.video.media.upload.UploadMediaUseCase;
import com.tresende.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.tresende.catalog.admin.application.video.retrieve.list.ListVideoUseCase;
import com.tresende.catalog.admin.application.video.upadate.UpdateVideoCommand;
import com.tresende.catalog.admin.application.video.upadate.UpdateVideoUseCase;
import com.tresende.catalog.admin.domain.castmember.CastMemberID;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.exceptions.NotificationException;
import com.tresende.catalog.admin.domain.genre.GenreID;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.resource.Resource;
import com.tresende.catalog.admin.domain.validation.Error;
import com.tresende.catalog.admin.domain.video.VideoMediaType;
import com.tresende.catalog.admin.domain.video.VideoResource;
import com.tresende.catalog.admin.domain.video.VideoSearchQuery;
import com.tresende.catalog.admin.infrastructure.api.VideoAPI;
import com.tresende.catalog.admin.infrastructure.utils.HashingUtils;
import com.tresende.catalog.admin.infrastructure.video.models.CreateVideoRequest;
import com.tresende.catalog.admin.infrastructure.video.models.UpdateVideoRequest;
import com.tresende.catalog.admin.infrastructure.video.models.VideoListResponse;
import com.tresende.catalog.admin.infrastructure.video.models.VideoResponse;
import com.tresende.catalog.admin.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static com.tresende.catalog.admin.domain.utils.CollectionUtils.mapTo;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final ListVideoUseCase listVideosUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final ListVideoUseCase listVideosUseCase,
            final GetMediaUseCase getMediaUseCase,
            final UploadMediaUseCase uploadMediaUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public Pagination<VideoListResponse> list(final String search, final int page, final int perPage, final String sort, final String direction, final Set<String> castMembers, final Set<String> categories, final Set<String> genres) {
        final var castMemberIDs = mapTo(castMembers, CastMemberID::from);
        final var categoriesIDs = mapTo(categories, CategoryID::from);
        final var genresIDs = mapTo(genres, GenreID::from);

        final var aQuery =
                new VideoSearchQuery(page, perPage, search, sort, direction, castMemberIDs, categoriesIDs, genresIDs);

        return VideoApiPresenter.present(listVideosUseCase.execute(aQuery));
    }

    @Override
    public ResponseEntity<?> createFull(
            final String aTitle,
            final String aDescription,
            final Integer launchedAt,
            final Double aDuration,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final String aRating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var aCmd = CreateVideoCommand.with(
                aTitle,
                aDescription,
                launchedAt,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile)
        );
        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
        final var aCmd = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );
        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponse getById(final String id) {
        return VideoApiPresenter.present(getVideoByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateVideoRequest payload) {
        final var aCmd = UpdateVideoCommand.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );
        final var output = this.updateVideoUseCase.execute(aCmd);
        return ResponseEntity
                .ok()
                .location(URI.create("/videos/" + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String id) {
        deleteVideoUseCase.execute(id);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String id, final String type) {
        final var command = GetMediaCommand.with(id, type);
        final var aMedia = getMediaUseCase.execute(command);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(aMedia.name()))
                .body(aMedia.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(final String id, final String type, final MultipartFile media) {
        final var videoMediaType = VideoMediaType.of(type).orElseThrow(() -> NotificationException.with(
                new Error("Invalid %s for VideoMediaType".formatted(type)))
        );

        final var resource = VideoResource.with(videoMediaType, resourceOf(media));
        final var aCommand = UploadMediaCommand.with(id, resource);
        final var output = uploadMediaUseCase.execute(aCommand);
        return ResponseEntity
                .created(URI.create("/videos/%s/medias/%s".formatted(id, type)))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) return null;

        try {
            return Resource.of(
                    HashingUtils.checksum(part.getBytes()),
                    part.getBytes(),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
