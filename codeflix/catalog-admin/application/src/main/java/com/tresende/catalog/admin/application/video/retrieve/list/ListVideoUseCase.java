package com.tresende.catalog.admin.application.video.retrieve.list;

import com.tresende.catalog.admin.application.UseCase;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.video.VideoSearchQuery;

public abstract class ListVideoUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}