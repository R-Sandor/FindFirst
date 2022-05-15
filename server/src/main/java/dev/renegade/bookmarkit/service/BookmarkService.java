
package dev.renegade.bookmarkit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.renegade.bookmarkit.model.Bookmark;
import dev.renegade.bookmarkit.repository.BookmarkRepository;
import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    public List<Bookmark> list() {
        return bookmarkRepository.findAll();
    }

    public Bookmark getById(Long id) {
        return bookmarkRepository.getById(id);
    }

    public void addBookmark(Bookmark bookmark){
        bookmarkRepository.saveAndFlush(bookmark);
    }

    public void addBookmarks(List<Bookmark> bookmarks){
        bookmarkRepository.saveAllAndFlush(bookmarks);
    }

    public void deleteBookmark(Bookmark bookmark) {
        bookmarkRepository.delete(bookmark);
    }

    public void deleteAllBookmarks(){
        bookmarkRepository.deleteAll();
    }

    public void deleteById(Long id){
        bookmarkRepository.deleteById(id);
    }


}