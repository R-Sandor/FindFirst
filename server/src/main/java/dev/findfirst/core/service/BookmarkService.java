package dev.findfirst.core.service;

import dev.findfirst.core.exceptions.BookmarkAlreadyExistsException;
import dev.findfirst.core.exceptions.TagNotFoundException;
import dev.findfirst.core.model.AddBkmkReq;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.ExportBookmark;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.model.TagBookmarks;
import dev.findfirst.core.repository.BookmarkRepository;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class BookmarkService {

  @Autowired private BookmarkRepository bookmarkRepository;

  @Autowired private TagService tagService;

  public List<Bookmark> list() {
    return bookmarkRepository.findAll();
  }

  public Optional<Bookmark> findById(Long id) {
    return id == null ? Optional.ofNullable(null) : bookmarkRepository.findById(id);
  }

  public Bookmark addBookmark(AddBkmkReq reqBkmk)
      throws BookmarkAlreadyExistsException, TagNotFoundException {
    var tags = new HashSet<Tag>();

    if (bookmarkRepository.findByUrl(reqBkmk.url()).isPresent()) {
      throw new BookmarkAlreadyExistsException();
    }

    if (reqBkmk.tagIds() != null) {
      for (var t : reqBkmk.tagIds()) {
        tags.add(tagService.findById(t).orElseThrow(() -> new TagNotFoundException()));
      }
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

  public String export() {
    var tags = tagService.getTags();
    var bkmkMap = new HashMap<Long, Long>();
    var uniqueBkmkWithTags = new ArrayList<TagBookmarks>();
    tags.sort(
        new Comparator<Tag>() {
          @Override
          public int compare(Tag lTag, Tag rTag) {
            return rTag.getBookmarks().size() - lTag.getBookmarks().size();
          }
        });
    // streams the sorted list
    tags.stream()
        .forEach(
            t -> {
              var unique = new ArrayList<Bookmark>();
              t.getBookmarks().stream()
                  .forEach(
                      b -> {
                        var found = bkmkMap.get(b.getId());
                        if (found == null) {
                          bkmkMap.put(b.getId(), t.getId());
                          unique.add(b);
                        }
                      });
              if (unique.size() > 0) {
                uniqueBkmkWithTags.add(new TagBookmarks(t.getTag_title(), unique));
              }
            });
    var exporter = new ExportBookmark(uniqueBkmkWithTags);
    return exporter.toString();
  }

  public void deleteBookmark(Long bookmarkId) {
    if (bookmarkId != null) {
      Optional<Bookmark> bookmark = bookmarkRepository.findById(bookmarkId);
      if (bookmark.isPresent()) {
        try {
          bookmarkRepository.deleteById(bookmarkId);
          bookmarkRepository.flush();

        } catch (ObjectOptimisticLockingFailureException exception) {
          // I don't know a fool proof way of preventing this error
          // other than blocking on the method. Which would not
          // be ideal given its a controller and the error itself
          // resolves.
        }
      }
    }
  }

  public void deleteAllBookmarks() {
    // Finds all that belong to the user and deletes them.
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

  public Flux<Bookmark> importBookmarks(String htmlFile) {
    var doc = Jsoup.parse(htmlFile);
    var hrefs = doc.getElementsByAttribute("href");
    hrefs.stream().forEach(e -> log.debug(e.attributes().get("href")));
    var sec = SecurityContextHolder.getContext();
    return Flux.fromStream(hrefs.stream())
        .map(
            el -> {
              String url = el.attributes().get("href");
              try {
                var retDoc = Jsoup.connect(url).get();
                log.debug(retDoc.title());
                // Issues with the context being lost between requests and database write.
                SecurityContextHolder.setContext(sec);
                String title = retDoc.title();
                if (!url.equals("")) {
                  title = title.equals("") ? url : title;
                  return addBookmark(new AddBkmkReq(title, url, null));
                }
              } catch (IOException | BookmarkAlreadyExistsException | TagNotFoundException ex) {
                log.error(ex.getMessage());
              }
              return new Bookmark();
            })
        .delayElements(Duration.ofMillis(100));
  }
}
