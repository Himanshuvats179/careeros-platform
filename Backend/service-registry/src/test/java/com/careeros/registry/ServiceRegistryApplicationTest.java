package com.careeros.registry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceRegistryApplicationTest {

    @Test
    @DisplayName("Should load Spring application context for Eureka Server")
    void contextLoads() {
        assertTrue(true, "Spring application context loaded successfully for Service Registry");
    }
}
