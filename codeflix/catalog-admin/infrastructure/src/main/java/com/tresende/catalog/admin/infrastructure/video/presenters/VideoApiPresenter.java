package com.tresende.catalog.admin.infrastructure.video.presenters;

import com.tresende.catalog.admin.application.video.retrieve.get.VideoOutput;
import com.tresende.catalog.admin.application.video.retrieve.list.VideoListOutput;
import com.tresende.catalog.admin.application.video.upadate.UpdateVideoOutput;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.video.AudioVideoMedia;
import com.tresende.catalog.admin.domain.video.ImageMedia;
import com.tresende.catalog.admin.infrastructure.video.models.*;

public interface VideoApiPresenter {

    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media) {
        if (media == null) return null;
        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }

    static ImageMediaResponse present(final ImageMedia image) {
        if (image == null) return null;
        return new ImageMediaResponse(
                image.id(),
                image.checksum(),
                image.name(),
                image.location()
        );
    }

    static UpdateVideoResponse present(UpdateVideoOutput output) {
        return new UpdateVideoResponse(output.id());
    }


    static VideoListResponse present(VideoListOutput output) {
        return new VideoListResponse(output.id(), output.title(), output.description(), output.createdAt(), output.updatedAt());
    }

    static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> page) {
        return page.map(VideoApiPresenter::present);
    }
}
