package com.tresende.catalog.admin.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    public void testeCreateUserCase() {
        Assertions.assertNotNull(new Main());
        Main.main(new String[]{});
    }
}