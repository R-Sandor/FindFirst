package dev.findfirst.bookmarkit.service;

import dev.findfirst.bookmarkit.model.Bookmark;
import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.repository.BookmarkRepository;
import dev.findfirst.bookmarkit.repository.TagRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

  @Autowired private BookmarkRepository bookmarkRepository;

  @Autowired private TagRepository tagRepository;

  public List<Bookmark> list() {
    return bookmarkRepository.findAll();
  }

  public Bookmark getById(Long id) {
    return bookmarkRepository.getReferenceById(id);
  }

  public void addBookmark(Bookmark bookmark) {
    bookmarkRepository.saveAndFlush(bookmark);
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
          tagRepository.deleteById(tag.getId());
        }
      };

  public void deleteById(Long id) {
    Set<Tag> tags = bookmarkRepository.getReferenceById(id).getTags();
    for (var tag : tags) {
      lConsumer.accept(bookmarkRepository.findByTag(tag), tag);
    }
  }

  public ResponseEntity<Tag> addTag(Long bookmarkId, Tag tagRequest) {
    Optional<Bookmark> bkmkOpt = bookmarkRepository.findById(bookmarkId);
    if (bkmkOpt.isPresent()) {
      Bookmark bookmark = bkmkOpt.get();

      // Find if tag exists and adds the record.
      if (addExistingTag(bookmark, tagRequest)) {
        return new ResponseEntity<>(tagRequest, HttpStatus.CREATED);
      }

      // Create a new tag record and add it to join table
      tagRequest.setBookmarks(new HashSet<>(Arrays.asList(bookmark)));

      // Create the new Tag.
      bookmark.addTag(tagRequest);
      tagRepository.save(tagRequest);
      return new ResponseEntity<>(tagRequest, HttpStatus.CREATED);
    }
    return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
  }

  public ResponseEntity<Tag> addTag(Long bookmarkId, Long tagId) {
    Bookmark bkmk = bookmarkRepository.findById(bookmarkId).get();
    Tag tag = tagRepository.findById(tagId).get();
    if (bkmk != null && tag != null && addExistingTag(bkmk, tag))
      return new ResponseEntity<>(tag, HttpStatus.CREATED);
    return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
  }

  private void addTagToBookmarkRecord(Bookmark bookmark, Long id) {
    Tag tag =
        tagRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Not found Tag with id = " + id));
    bookmark.addTag(tag);
    bookmarkRepository.save(bookmark);
  }

  private boolean addExistingTag(Bookmark bookmark, Tag tagRequest) {
    if (tagRequest.getId() == 0L) {
      Tag tag = tagRepository.findByTagTitle(tagRequest.getTag_title());
      if (tag != null) {
        addTagToBookmarkRecord(bookmark, tag.getId());
        return true;
      }
    } else {
      addTagToBookmarkRecord(bookmark, tagRequest.getId());
      return true;
    }
    return false;
  }
}
