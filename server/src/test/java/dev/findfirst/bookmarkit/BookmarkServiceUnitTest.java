package dev.findfirst.bookmarkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.bookmarkit.model.Bookmark;
import dev.findfirst.bookmarkit.service.BookmarkService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookmarkServiceUnitTest {

  @Autowired private BookmarkService bookmarkService;

  @Test
  public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
    List<Bookmark> bookmarks = bookmarkService.list();
    assertEquals(bookmarks.size(), 2);
  }


  
}
