package dev.findfirst.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.findfirst.core.repository.BookmarkRepository;
import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseTest {

  @MockBean private TenantContext tenantContext;

  @Container @ServiceConnection
  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.4");

  @Autowired BookmarkRepository bkmkRepo;

  @Test
  void connectionEstablish() {
    assertThat(postgres.isCreated()).isTrue();
  }

  @Test
  void repoLoads() {
    assertNotNull(bkmkRepo);
    var bkmks = bkmkRepo.findAll();
    assertTrue(bkmks.size() > 0);
    // Check that data.sql is loading.
    assertEquals(bkmks.size(), 4);
  }
}
