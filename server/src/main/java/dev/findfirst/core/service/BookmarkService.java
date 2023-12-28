package dev.findfirst.core.service;

import dev.findfirst.core.model.AddBkmkReq;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.repository.BookmarkRepository;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

    var newBkmk = new Bookmark(reqBkmk.title(), reqBkmk.url());
    newBkmk.setTags(tags);
    return bookmarkRepository.save(newBkmk);
  }

  public void addBookmarks(List<Bookmark> bookmarks) {
    bookmarkRepository.saveAllAndFlush(bookmarks);
  }

  public void deleteBookmark(Long bookmarkId) {
    bookmarkRepository.deleteById(bookmarkId);
  }

  public void deleteAllBookmarks() {
    // finds all that belong to the user and deletes them.
    // Otherwise the @preRemove throws an execption as it should.
    bookmarkRepository.deleteAll(bookmarkRepository.findAll());
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
