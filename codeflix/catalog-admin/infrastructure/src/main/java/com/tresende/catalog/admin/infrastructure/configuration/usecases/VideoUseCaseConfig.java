package com.tresende.catalog.admin.infrastructure.configuration.usecases;

import com.tresende.catalog.admin.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.tresende.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.tresende.catalog.admin.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase UpdateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
