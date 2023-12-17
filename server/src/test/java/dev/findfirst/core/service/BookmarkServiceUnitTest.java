package dev.findfirst.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.core.annotations.IntegrationTestConfig;
import dev.findfirst.core.model.Bookmark;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@IntegrationTestConfig
public class BookmarkServiceUnitTest {

  @Autowired private BookmarkService bookmarkService;

  @Test
  public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
    List<Bookmark> bookmarks = bookmarkService.list();
    assertEquals(bookmarks.size(), 2);
  }
}
