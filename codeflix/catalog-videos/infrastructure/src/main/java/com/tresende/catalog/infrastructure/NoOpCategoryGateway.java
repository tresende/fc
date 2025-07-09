package com.tresende.catalog.infrastructure;

import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.utils.InstantUtils;
import com.tresende.catalog.infrastructure.category.CategoryGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NoOpCategoryGateway implements CategoryGateway {
    @Override
    public Optional<Category> categoryOfId(final String anId) {
        return Optional.of(Category.with(
                anId,
                "Lives",
                null,
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        ));
    }
}
