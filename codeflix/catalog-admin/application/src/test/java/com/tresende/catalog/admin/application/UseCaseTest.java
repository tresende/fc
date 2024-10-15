package com.tresende.catalog.admin.application;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {
    protected abstract List<Object> getMocks();

    @Override
    public void beforeEach(final ExtensionContext extensionContext) {
        reset(getMocks());
    }
}