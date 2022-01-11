package dev.renegade.bookmarkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import dev.renegade.bookmarkit.model.Bookmark;
import dev.renegade.bookmarkit.service.BookmarkService;

@ExtendWith(SpringExtension.class)@SpringBootTest
public class BookmarkServiceUnitTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<Bookmark> bookmarks = bookmarkService.list();
        assertEquals(bookmarks.size(), 2);
    }
}