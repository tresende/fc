package com.tresende.catalog;

import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;

@ActiveProfiles("test-integration")
@ComponentScan(
        basePackages = {"com.tresende.catalog"},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*ElasticsearchGateway")
        }
)
@DataElasticsearchTest
@ImportTestcontainers(ElasticSearchTestContainer.class)
@Testcontainers
@Tag("integrationTest")
public class AbstractElasticsearchTest {

    @Autowired
    private Collection<ElasticsearchRepository<?, ?>> repositories;

    void cleanUp() {
        repositories.forEach(ElasticsearchRepository::deleteAll);
    }
}
