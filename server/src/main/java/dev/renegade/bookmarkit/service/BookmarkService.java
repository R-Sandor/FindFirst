
package dev.renegade.bookmarkit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.renegade.bookmarkit.model.Bookmark;
import dev.renegade.bookmarkit.BookmarkRepository;
import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    public List<Bookmark> list() {
        System.out.println("list");
        return bookmarkRepository.findAll();
    }
}