package com.tresende.catalog.infrastructure.kafka;

import com.tresende.catalog.AbstractEmbeddedKafkaTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryListenerTest extends AbstractEmbeddedKafkaTest {

    @Test
    public void testDummy() {
        Assertions.assertNotNull(producer());
    }
}
