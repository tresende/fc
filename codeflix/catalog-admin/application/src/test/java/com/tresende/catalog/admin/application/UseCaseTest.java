package com.tresende.catalog.admin.application;

import com.tresende.catalog.admin.domain.Identifier;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {


    protected List<String> asString(List<? extends Identifier> categories) {
        return categories.stream().map(Identifier::getValue).toList();
    }


    protected Set<String> asString(Set<? extends Identifier> categories) {
        return categories.stream().map(Identifier::getValue).collect(Collectors.toSet());
    }

    protected abstract List<Object> getMocks();

    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        reset(getMocks());
    }
}