package dev.findfirst.bookmarkit.service;

import dev.findfirst.bookmarkit.model.Bookmark;
import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.repository.BookmarkRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

  @Autowired private BookmarkRepository bookmarkRepository;

  @Autowired private TagService tagService;

  public List<Bookmark> list() {
    return bookmarkRepository.findAll();
  }

  public Optional<Bookmark> findById(Long id) {
    return bookmarkRepository.findById(id);
  }

  public Bookmark addBookmark(Bookmark bookmark) {
    return bookmarkRepository.saveAndFlush(bookmark);
  }

  public void addBookmarks(List<Bookmark> bookmarks) {
    bookmarkRepository.saveAllAndFlush(bookmarks);
  }

  public void deleteBookmark(Bookmark bookmark) {
    bookmarkRepository.delete(bookmark);
  }

  public void deleteAllBookmarks() {
    bookmarkRepository.deleteAll();
  }

  private BiConsumer<List<Bookmark>, Tag> lConsumer =
      (list, tag) -> {
        if (list.size() == 1) {
          tagService.deleteTag(tag);
        }
      };

  public void deleteById(Long id) {
    Set<Tag> tags = bookmarkRepository.getReferenceById(id).getTags();
    for (var tag : tags) {
      lConsumer.accept(bookmarkRepository.findByTag(tag), tag);
    }
  }

  public ResponseEntity<? super Tag> addTagToBookmark(Long bookmarkId, String title) {
    var resp =
        new Object() {
          ResponseEntity<? super Tag> resp;
        };
    tagService
        .findByTagTitle(title)
        .ifPresentOrElse(
            (Tag t) -> {
              resp.resp = new ResponseEntity<Tag>(t, HttpStatus.OK);
            },
            () -> resp.resp = new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    return resp.resp;
  }

  public ResponseEntity<Bookmark> addTagToBookmark(Bookmark bookmark, Tag tag) {
    if (bookmark == null || tag == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    bookmark.addTag(tag);
    bookmarkRepository.save(bookmark);
    return new ResponseEntity<Bookmark>(bookmark, HttpStatus.OK);
  }
}
