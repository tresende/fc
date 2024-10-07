package com.tresende.catalog.admin.infrastructure;

import com.tresende.catalog.admin.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");

        SpringApplication.run(WebServerConfig.class);
    }

//    @Bean
//    @DependsOnDatabaseInitialization
//    ApplicationRunner runner(
//            CreateCategoryUseCase createCategoryUseCase,
//            UpdateCategoryUseCase updateCategoryUseCase,
//            DeleteCategoryUseCase deleteCategoryUseCase,
//            ListCategoryUseCase listCategoryUseCase,
//            GetCategoryByUseCase getCategoryByUseCase
//    ) {
//        return args -> {
//            System.out.println(createCategoryUseCase);
//            System.out.println(updateCategoryUseCase);
//            System.out.println(deleteCategoryUseCase);
//            System.out.println(listCategoryUseCase);
//            System.out.println(getCategoryByUseCase);
//        };
//    }
}