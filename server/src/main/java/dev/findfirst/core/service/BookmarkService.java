package dev.findfirst.core.service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import jakarta.validation.constraints.NotNull;

import dev.findfirst.core.exceptions.BookmarkAlreadyExistsException;
import dev.findfirst.core.exceptions.TagNotFoundException;
import dev.findfirst.core.model.AddBkmkReq;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.ExportBookmark;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.model.TagBookmarks;
import dev.findfirst.core.repository.BookmarkRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;

  private final TagService tagService;

  private final ScreenshotManager sManager;

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

    var optUrl = sManager.getScreenshot(reqBkmk.url());

    var newBkmk = new Bookmark(reqBkmk.title(), reqBkmk.url(), optUrl.orElseGet(() -> ""));
    newBkmk.setTags(tags);
    return bookmarkRepository.save(newBkmk);
  }

  public List<Bookmark> addBookmarks(List<AddBkmkReq> bookmarks) throws Exception {
    return bookmarks.stream().map(t -> {
      try {
        return addBookmark(t);
      } catch (Exception e) {
        return null;
      }
    }).toList();
  }

  /**
   * Exports bookmarks by their tag groups. The largest tag groups are exported first. Any bookmark
   * already accounted for in that group will be excluded from any other group that it was also
   * tagged.
   *
   * @return String representing HTLM file.
   */
  public String export() {
    var tags = tagService.getTags();
    var foundMap = new HashMap<Long, Long>();
    var uniqueBkmksWithTag = new ArrayList<TagBookmarks>();

    // Sort by the largest tags set.
    tags.sort(new Comparator<Tag>() {
      @Override
      public int compare(Tag lTag, Tag rTag) {
        return rTag.getBookmarks().size() - lTag.getBookmarks().size();
      }
    });

    // streams the sorted list
    tags.stream().forEach(t -> {
      var uniques = new ArrayList<Bookmark>();
      addUniqueBookmarks(t, uniques, foundMap, uniqueBkmksWithTag);
    });
    var exporter = new ExportBookmark(uniqueBkmksWithTag);
    return exporter.toString();
  }

  /**
   * Checks if a bookmark has already been found in previous tag group. If it has not it is added to
   * uniques, and the id added to map for fast lookups. Finally record that contains the title of
   * the tag `cooking` `docs` for example is created with it associated bookmarks. The record is
   * added to uniqueBkmkWithTags.
   *
   * @param t Tag
   * @param uniques List<Bookmark> of uniques
   * @param alreadyFound Map<Long, Long> for fast lookup
   * @param uniqueBkmksWithTag Record of Tag Title with Bookmark.
   */
  private void addUniqueBookmarks(Tag t, List<Bookmark> uniques, Map<Long, Long> alreadyFound,
      List<TagBookmarks> uniqueBkmksWithTag) {
    t.getBookmarks().stream().forEach(bkmk -> {
      var found = alreadyFound.get(bkmk.getId());
      if (found == null) {
        alreadyFound.put(bkmk.getId(), bkmk.getId());
        uniques.add(bkmk);
      }
    });
    if (uniques.size() > 0) {
      uniqueBkmksWithTag.add(new TagBookmarks(t.getTag_title(), uniques));
    }
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
    if (bookmark == null || tag == null)
      throw new NoSuchFieldError();
    bookmark.addTag(tag);
    bookmarkRepository.save(bookmark);
    return tag;
  }

  public Tag deleteTag(long id, @NotNull Tag tag) {
    final var bkmk = bookmarkRepository.findById(id);
    bkmk.ifPresent((b) -> {
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
    return Flux.fromStream(hrefs.stream()).map(el -> {
      String url = el.attributes().get("href");
      try {
        var retDoc = Jsoup.connect(url).get();
        log.debug("Response {}",  retDoc.connection().response().statusMessage());
        log.debug(retDoc.title());
        // Issues with the context being lost between requests and database write.
        SecurityContextHolder.setContext(sec);
        String title = retDoc.title();
        if (!url.equals("") || url == null) {
          title = (title.equals("") || title == null) ? url : title;
          log.debug("Bookmark contains: \n\t{},\n\t{}", title, url);
          return addBookmark(new AddBkmkReq(title, url, null));
        }
      } catch (IOException | BookmarkAlreadyExistsException | TagNotFoundException ex) {
        log.error(ex.getMessage());
      }
      return new Bookmark();
    }).delayElements(Duration.ofMillis(100));
  }
}
