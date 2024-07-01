package dev.findfirst.core.service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import dev.findfirst.core.model.AddBkmkReq;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.repository.BookmarkRepository;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;

@Service
public class BookmarkService {

  @Autowired private BookmarkRepository bookmarkRepository;

  @Autowired private TagService tagService;

  public List<Bookmark> list() {
    return bookmarkRepository.findAll();
  }

  public Optional<Bookmark> findById(Long id) {
    return id == null ? Optional.ofNullable(null) : bookmarkRepository.findById(id);
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

  public List<Bookmark> addBookmarks(List<AddBkmkReq> bookmarks) throws Exception {
    return bookmarks.stream()
        .map(
            t -> {
              try {
                return addBookmark(t);
              } catch (Exception e) {
                return null;
              }
            })
        .toList();
  }

  public void deleteBookmark(Long bookmarkId) {
    if (bookmarkId != null) {
      Optional<Bookmark> bookmark = bookmarkRepository.findById(bookmarkId);
      if (bookmark.isPresent()) {
        try {
          bookmarkRepository.deleteById(bookmarkId);
          bookmarkRepository.flush();

        } catch (ObjectOptimisticLockingFailureException exception) {
          // I don't know a fool proof way of preventing this error other than blocking on
          // the method.
          // Which would not be ideal given its a controller and the error itself
          // resolves.
        }
      }
    }
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

  private Flux<Bookmark> stream() {
    return Flux.range(1, 3)
        .delayElements(Duration.ofSeconds(1))
        .map(i -> new Bookmark("bk" + i, i.toString()));
  }


  public Flux<Bookmark> importBookmarks(String htmlFile) { 

    var doc = Jsoup.parse(htmlFile);
    System.out.println(doc.title());

      return stream();
  } 
}
