package com.tresende.catalog.admin.infrastructure.configuration.usecases;

import com.tresende.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.tresende.catalog.admin.application.category.create.DefaultCreateCategoryUseCase;
import com.tresende.catalog.admin.application.category.delete.DefaultDeleteCategoryUseCase;
import com.tresende.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.tresende.catalog.admin.application.category.retrieve.get.DefaultGetCategoryByUseCase;
import com.tresende.catalog.admin.application.category.retrieve.get.GetCategoryByUseCase;
import com.tresende.catalog.admin.application.category.retrieve.list.DefaultListCategoryUseCase;
import com.tresende.catalog.admin.application.category.retrieve.list.ListCategoryUseCase;
import com.tresende.catalog.admin.application.category.update.DefaultUpdateCategoryUseCase;
import com.tresende.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByUseCase getCategoryByUseCase() {
        return new DefaultGetCategoryByUseCase(categoryGateway);
    }
}
