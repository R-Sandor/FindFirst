package dev.findfirst.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.HashSet;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.model.jdbc.BookmarkJDBC;
import dev.findfirst.core.model.jdbc.BookmarkTag;
import dev.findfirst.core.model.jdbc.TagJDBC;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;
import dev.findfirst.core.repository.jdbc.BookmarkTagRepository;
import dev.findfirst.core.repository.jdbc.TagJDBCRepository;
import dev.findfirst.core.service.TagService;
import dev.findfirst.security.userauth.context.UserContext;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IntegrationTest
@TestMethodOrder(OrderAnnotation.class)
class DatabaseTest {

  @MockitoBean
  private UserContext userContext;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  final BookmarkJDBCRepository bkmkJDBCRepo;
  final TagService tagService;
  final TagJDBCRepository tagRepo;
  final BookmarkTagRepository bookmarkTagRepository;

  @Autowired
  DatabaseTest(TagService tagService, BookmarkJDBCRepository bookmarkJDBCRepository,
      TagJDBCRepository tagRepo, BookmarkTagRepository btRepo) {
    this.bkmkJDBCRepo = bookmarkJDBCRepository;
    this.tagService = tagService;
    this.tagRepo = tagRepo;
    this.bookmarkTagRepository = btRepo;
  }

  @Test
  @Order(4)
  void connectionEstablish() {
    assertThat(postgres.isCreated()).isTrue();
  }

  @Test
  @Order(3)
  void getAllBookmarksForTag() {
    Mockito.when(userContext.getUserId()).thenReturn(1);
    var tag = tagService.getTagWithBookmarks(1l);
    assertEquals(2, tag.bookmarks().size());
  }

  @Test
  @Order(1)
  void getAllTagIdsForUsersBookmarks() {
    int tenatId = 1;
    var tags = bookmarkTagRepository.getUserAllTagIdsToBookmarks(tenatId);
    assertEquals(4, tags.size());
    var tagSet = bookmarkTagRepository.getAllTagIdsForBookmark(1l, tenatId);
    assertEquals(2, tagSet.size());
  }

  @Test()
  @Order(2)
  void addTagToBookmark() {
    int userId = 1;
    var bookmark = new BookmarkJDBC(null, userId, new Date(), "test", "test", new Date(),
        "My very cool bookmark", "https://test.com", "test.com", true, new HashSet<>());
    var bkmkEnt = bkmkJDBCRepo.save(bookmark);

    var tagEnt = tagRepo.save(new TagJDBC(null, userId, new Date(System.currentTimeMillis()),
        "test", "test", new Date(System.currentTimeMillis()), "TestTAG"));

    BookmarkTag bookmarkTag = new BookmarkTag(bkmkEnt.getId(), tagEnt.getId());

    bookmarkTagRepository.saveBookmarkTag(bookmarkTag);

    var tagSet = bookmarkTagRepository.getAllTagIdsForBookmark(bkmkEnt.getId(), userId);
    assertEquals(1, tagSet.size());

  }

}
