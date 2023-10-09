package dev.findfirst.bookmarkit.service;

import dev.findfirst.bookmarkit.model.AddBkmkReq;
import dev.findfirst.bookmarkit.model.Bookmark;
import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.repository.BookmarkRepository;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
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

  public Bookmark addBookmark(AddBkmkReq reqBkmk) throws Exception {
    var tags = new HashSet<Tag>();

    if (bookmarkRepository.findByUrl(reqBkmk.url()).isPresent()) {
      throw new Exception("bookmark exists");
    }

    for (var t : reqBkmk.tagIds()) {
      tags.add(tagService.findById(t).orElseThrow(() -> new Exception("No such tag exists")));
    }

    var newBkmk = new Bookmark(null, reqBkmk.title(), reqBkmk.url(), tags);
    return bookmarkRepository.save(newBkmk);
  }

  public void addBookmarks(List<Bookmark> bookmarks) {
    bookmarkRepository.saveAllAndFlush(bookmarks);
  }

  public void deleteBookmark(Long bookmarkId) { 
    bookmarkRepository.deleteById(bookmarkId);
  }

  public void deleteAllBookmarks() {
    bookmarkRepository.deleteAll();
  }

  private BiConsumer<List<Bookmark>, Tag> tagDelete =
      (list, tag) -> {
        if (list.size() == 1) {
          tagService.deleteTag(tag);
        }
      };

  public void deleteById(Long id) {
    Set<Tag> tags = bookmarkRepository.getReferenceById(id).getTags();
    for (var tag : tags) {
      tagDelete.accept(bookmarkRepository.findByTag(tag), tag);
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

  public Tag addTagToBookmark(Bookmark bookmark, Tag tag) {
    if (bookmark == null || tag == null) throw new NoSuchFieldError();
    bookmark.addTag(tag);
    bookmarkRepository.save(bookmark);
    return tag;
  }

  public Tag deleteTag(long id, @NotNull Tag tag) {
    final var bkmk = bookmarkRepository.findById(id);
    bkmk.ifPresent(
        (b) -> {
          b.removeTag(tag);
          bookmarkRepository.save(b);
        });
    return tag;
  }
}
