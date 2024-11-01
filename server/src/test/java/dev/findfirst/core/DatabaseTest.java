package dev.findfirst.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.HashSet;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.model.jdbc.BookmarkJDBC;
import dev.findfirst.core.model.jdbc.BookmarkTag;
import dev.findfirst.core.model.jdbc.BookmarkTagID;
import dev.findfirst.core.model.jdbc.TagJDBC;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;
import dev.findfirst.core.repository.jdbc.BookmarkTagRepository;
import dev.findfirst.core.repository.jdbc.TagJDBCRepository;
import dev.findfirst.core.repository.jpa.BookmarkRepository;
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

  final BookmarkJDBCRepository bkmkJDBCRepo;
  final TagService tagService;
  final TagJDBCRepository tagRepo;
  final BookmarkTagRepository bookmarkTagRepository;

  @Autowired
  DatabaseTest(BookmarkRepository bkmkRepo, TagService tagService,
      BookmarkJDBCRepository bookmarkJDBCRepository, TagJDBCRepository tagRepo,
      BookmarkTagRepository btRepo) {
    this.bkmkJDBCRepo = bookmarkJDBCRepository;
    this.tagService = tagService;
    this.tagRepo = tagRepo;
    this.bookmarkTagRepository = btRepo;
  }

  @Test
  void connectionEstablish() {
    assertThat(postgres.isCreated()).isTrue();
  }

  @Test
  void getAllBookmarksForTag() {
    var tag = tagService.getTagWithBookmarks(1l);
    assertEquals(2, tag.getBookmarks().size());
  }

  @Test
  void getAllTagIdsForUsersBookmarks() {
    int tenatId = 1;
    var tags = bookmarkTagRepository.getUserAllTagIdsToBookmarks(tenatId);
    assertEquals(4, tags.size());
    var tagSet = bookmarkTagRepository.getAllTagIdsForBookmark(1l, tenatId);
    assertEquals(2, tagSet.size());
  }

  @Test
  void addTagToBookmark() {
    int tenantId = 1;
    var bookmark = new BookmarkJDBC(null, tenantId, new Date(System.currentTimeMillis()), "test",
        "test", new Date(System.currentTimeMillis()), "My very cool bookmark", "https://test.com",
        "test.com", true, new HashSet<>());
    var bkmkEnt = bkmkJDBCRepo.save(bookmark);
    System.out.println(bkmkEnt);

    var tagEnt = tagRepo.save(new TagJDBC(null, tenantId, new Date(System.currentTimeMillis()),
        "test", "test", new Date(System.currentTimeMillis()), "TestTAG", new HashSet<>()));

    BookmarkTagID bookmarkTagID = new BookmarkTagID(bkmkEnt.getId(), tagEnt.getId());
    BookmarkTag bookmarkTag = new BookmarkTag(bkmkEnt.getId(), tagEnt.getId());

    bookmarkTagRepository.update(bookmarkTag);
  }

}
