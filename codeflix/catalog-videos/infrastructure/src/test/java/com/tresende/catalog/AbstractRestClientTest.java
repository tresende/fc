package com.tresende.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tresende.catalog.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-integration")
@EnableAutoConfiguration(exclude = {
        KafkaAutoConfiguration.class,
        ElasticsearchRepositoriesAutoConfiguration.class
})
@SpringBootTest(classes = {
        WebServerConfig.class,
        IntegrationTestConfiguration.class
})
@AutoConfigureWireMock(port = 0)
public class AbstractRestClientTest {
    @Autowired
    public ObjectMapper objectMapper;

    public String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
