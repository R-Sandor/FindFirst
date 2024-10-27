package dev.findfirst.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.repository.BookmarkRepository;
import dev.findfirst.core.service.TagService;
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
@IntegrationTest
public class DatabaseTest {

  @MockBean
  private TenantContext tenantContext;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  final BookmarkRepository bkmkRepo;
  final TagService tagService;

  @Autowired
  DatabaseTest(BookmarkRepository bkmkRepo, TagService tagService) {
    this.bkmkRepo = bkmkRepo;
    this.tagService = tagService;
  }

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

  @Test
  void getAllBookmarksForTag() {
    var tag = tagService.getTagWithBookmarks(1l);
    System.out.println(tag.getBookmarks());
    assertEquals(2, tag.getBookmarks().size());
  }
}
