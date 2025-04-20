package com.tresende.catalog;

import com.tresende.catalog.infrastructure.Main;
import com.tresende.catalog.infrastructure.configuration.ObjectMapperConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@ContextConfiguration(classes = Main.class)
@GraphQlTest(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ObjectMapperConfig.class)
)
@Tag("integrationTest")
public @interface GraphQLControllerTest {

    @AliasFor(annotation = GraphQlTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}