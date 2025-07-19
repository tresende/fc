package com.tresende.catalog;

import com.tresende.catalog.infrastructure.configuration.WebServerConfig;
import com.tresende.catalog.infrastructure.kafka.models.connect.Source;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@ActiveProfiles("test-integration")
@EnableAutoConfiguration(exclude = {
        ElasticsearchRepositoriesAutoConfiguration.class,
})
@SpringBootTest(
        classes = {WebServerConfig.class, IntegrationTestConfiguration.class},
        properties = {"kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"}
)
@Tag("integrationTest")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractEmbeddedKafkaTest {
    protected Producer producer;
    @Autowired
    protected EmbeddedKafkaBroker kafkaBroker;
    private AdminClient adminClient;

    @BeforeAll
    void init() {
        adminClient = AdminClient.create(Collections.singletonMap(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker.getBrokersAsString()));

        producer =
                new DefaultKafkaProducerFactory<>(KafkaTestUtils.producerProps(kafkaBroker), new StringSerializer(), new StringSerializer())
                        .createProducer();
    }

    @AfterAll
    void shutdown() {
        producer.close();
    }


    public Producer<String, String> producer() {
        return producer;
    }

    public AdminClient admin() {
        return adminClient;
    }

    public Source aSource() {
        return new Source("admin_mysql", "admin_catalog", "categories");
    }
}
