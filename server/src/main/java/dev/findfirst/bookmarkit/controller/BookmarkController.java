package dev.findfirst.bookmarkit.controller;

import dev.findfirst.bookmarkit.model.Bookmark;
import dev.findfirst.bookmarkit.model.PairWrapper;
import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.service.BookmarkService;
import dev.findfirst.bookmarkit.service.TagService;
import dev.findfirst.bookmarkit.utilies.Response;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookmarkController {

  @Autowired private BookmarkService bookmarkService;

  @Autowired private TagService tagService;

  @GetMapping("/bookmarks")
  public ResponseEntity<List<Bookmark>> getAllBookmarks() {
    return new Response<List<Bookmark>>(bookmarkService.list(), HttpStatus.OK).get();
  }

  @GetMapping(value = "bookmark/{id}")
  public ResponseEntity<Bookmark> getBookmarkById(@RequestParam Long id) {
    return new Response<Bookmark>(bookmarkService.findById(id)).get();
  }

  @PostMapping(value = "/bookmark/add")
  public ResponseEntity<Bookmark> addBookmarks(@RequestBody Bookmark bookmark) {
    return new Response<Bookmark>((b) -> bookmarkService.addBookmark(bookmark), bookmark).get();
  }

  @PostMapping(value = "/bookmark/addBookmarks")
  public ResponseEntity<List<Bookmark>> postMethodName(@RequestBody List<Bookmark> bookmarks) {
    return new Response<List<Bookmark>>((b) -> bookmarkService.addBookmarks(b), bookmarks).get();
  }

  @PostMapping(value = "/bookmark/deleteAll")
  public void deleteAll() {
    // TODO: handle deleting the associated bookmark_tag
    bookmarkService.deleteAllBookmarks();
  }

  @PostMapping("/bookmark/{bookmarkId}/addTag")
  public ResponseEntity<Bookmark> addTag(
      @PathVariable(value = "bookmarkId") Long bookmarkId, @RequestBody Tag tagRequest) {
    final var bookmark = bookmarkService.findById(bookmarkId);
    Consumer<Bookmark> action =
        (b) -> {
          bookmarkService.addTagToBookmark(b, tagRequest);
        };
    return new Response<Bookmark>(action, bookmark).get();
  }

  @PostMapping("/bookmark/addTag/{bookmarkId}/{tagId}")
  public ResponseEntity<PairWrapper<Bookmark, Tag>> addTag(
      @PathVariable(value = "bookmarkId") Long bookmarkId,
      @PathVariable(value = "tagId") Long tagId) {

    var bookmark = bookmarkService.findById(bookmarkId);
    var tag = tagService.findById(tagId);

    Consumer<PairWrapper<Bookmark, Tag>> action =
        (PairWrapper<Bookmark, Tag> bt) -> {
          bookmarkService.addTagToBookmark(bt.left(), bt.right());
        };

    if (bookmark.isPresent() && tag.isPresent()) {
      var resp = new Response<PairWrapper<Bookmark, Tag>>();
      resp.prepareResponse(action, new PairWrapper<Bookmark, Tag>(bookmark.get(), tag.get()));
      return resp.get();
    }
    return ResponseEntity.badRequest().build();
  }

  @PostMapping(value = "/bookmark/delete/{id}")
  public void deleteById(@PathVariable Long id) {
    bookmarkService.deleteById(id);
  }
}
