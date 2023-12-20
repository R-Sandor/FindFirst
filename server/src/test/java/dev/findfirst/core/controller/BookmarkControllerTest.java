package dev.findfirst.core.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.core.annotations.IntegrationTestConfig;
import dev.findfirst.core.repository.BookmarkRepository;
import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IntegrationTestConfig
public class BookmarkControllerTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @Autowired BookmarkRepository bkmkRepo;
  @MockBean private TenantContext tenantContext;

  @Test
  void test() {
    assertEquals(postgres.isRunning(), true);
    // assertNotNull(bkmkRepo);
  }
}
