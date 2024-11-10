package com.tresende.catalog.admin.infrastructure.castmember;

import com.tresende.catalog.admin.MySQLGatewayTest;
import com.tresende.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway categoryGateway;

    @Autowired
    private CastMemberRepository categoryRepository;

    @Test
    public void testDependencies() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(categoryRepository);
    }
}
