package com.tresende.catalog.admin.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UseCaseTest {
    @Test
    public void testeCreateUserCase() {
        Assertions.assertNotNull(new UseCase());
    }
}