package dev.renegade.bookmarkit.controller;

import dev.renegade.bookmarkit.model.Bookmark;
import dev.renegade.bookmarkit.model.Tag;
import dev.renegade.bookmarkit.service.BookmarkService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookmarkController {

  @Autowired
  private BookmarkService bookmarkService;

  @RequestMapping(
    value = "/bookmarks",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public List<Bookmark> getAllBookmarks() {
    return bookmarkService.list();
  }

  @RequestMapping(
    value = "/bookmark/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Bookmark getBookmarkById(@RequestParam Long id) {
    return bookmarkService.getById(id);
  }

  @PostMapping(value = "/bookmark/add")
  public void addBookmarks(@RequestBody Bookmark bookmark) {
    bookmarkService.addBookmark(bookmark);
  }

  @PostMapping(value = "/bookmark/addBookmarks")
  public void postMethodName(@RequestBody List<Bookmark> bookmarks) {
    bookmarkService.addBookmarks(bookmarks);
  }

  @PostMapping(value = "/bookmark/deleteAll")
  public void deleteAll() {
    // TODO: handle deleting the associated bookmark_tag
    bookmarkService.deleteAllBookmarks();
  }

  @PostMapping("/bookmark/{bookmarkId}/addTag")
  public ResponseEntity<Tag> addTag(
    @PathVariable(value = "bookmarkId") Long bookmarkId,
    @RequestBody Tag tagRequest
  ) {
    return bookmarkService.addTag(bookmarkId, tagRequest);
  }

  @PostMapping("/bookmark/addTag/{bookmarkId}/{tagId}")
  public ResponseEntity<Tag> addTag(
    @PathVariable(value = "bookmarkId") Long bookmarkId,
    @PathVariable(value = "tagId") Long tagId
  ) {
    return bookmarkService.addTag(bookmarkId, tagId);
  }

  @PostMapping(value = "/bookmark/delete/{id}")
  public void deleteById(@PathVariable Long id) {
    bookmarkService.deleteById(id);
  }
}
