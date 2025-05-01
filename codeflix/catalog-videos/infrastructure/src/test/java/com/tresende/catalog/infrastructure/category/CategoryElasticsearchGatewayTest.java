package com.tresende.catalog.infrastructure.category;


import com.tresende.catalog.AbstractElasticsearchTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(categoryRepository);
    }
}
