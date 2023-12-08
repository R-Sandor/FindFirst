package dev.findfirst.bookmarkit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.bookmarkit.annotations.IntegrationTestConfig;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.service.BookmarkService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.EnabledIf;

// @DataJpaTest
@IntegrationTestConfig
@EnabledIf(
    value = "#{{'test', 'prod'}.contains(environment.getActiveProfiles()[0])}",
    loadContext = true)
public class BookmarkServiceUnitTest {

  @Autowired private BookmarkService bookmarkService;

  @Test
  public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
    List<Bookmark> bookmarks = bookmarkService.list();
    assertEquals(bookmarks.size(), 2);
  }
}
