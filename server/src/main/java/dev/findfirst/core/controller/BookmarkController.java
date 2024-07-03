package dev.findfirst.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.findfirst.core.model.AddBkmkReq;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.BookmarkTagPair;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.service.BookmarkService;
import dev.findfirst.core.service.TagService;
import dev.findfirst.core.utilies.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@Validated
@RequestMapping("/api")
public class BookmarkController {

  @Autowired private BookmarkService bookmarkService;

  @Autowired private TagService tagService;

  @GetMapping("/bookmarks")
  public ResponseEntity<List<Bookmark>> getAllBookmarks() {
    return new Response<List<Bookmark>>(bookmarkService.list(), HttpStatus.OK).get();
  }

  @DeleteMapping(value = "/bookmarks")
  public ResponseEntity<String> deleteAll() {
    bookmarkService.deleteAllBookmarks();
    return ResponseEntity.ok("Deleted All user's bookmarks");
  }

  @GetMapping(value = "/bookmark")
  public ResponseEntity<Bookmark> getBookmarkById(@RequestParam("id") long id) {
    return new Response<Bookmark>(bookmarkService.findById(id)).get();
  }

  @PostMapping(value = "/bookmark")
  public ResponseEntity<Bookmark> addBookmarks(@RequestBody AddBkmkReq req) {
    Bookmark createdBookmark;
    var response = new Response<Bookmark>();
    try {
      createdBookmark = bookmarkService.addBookmark(req);
      return response.setResponse(createdBookmark, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return response.setResponse(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping(value = "/bookmark", produces = "application/json")
  public ResponseEntity<String> deleteById(@RequestParam("id") Long id)
      throws JsonProcessingException {
    bookmarkService.deleteBookmark(id);
    ObjectMapper mapper = new ObjectMapper();
    String jsonStr = mapper.writeValueAsString("Deleted Bookmark %s".formatted(id));
    return new ResponseEntity<String>(jsonStr, HttpStatus.OK);
  }

  @PostMapping(value = "/bookmark/addBookmarks")
  public ResponseEntity<List<Bookmark>> addBookmarks(@RequestBody List<AddBkmkReq> bookmarks) {
    try {
      return new ResponseEntity<List<Bookmark>>(
          bookmarkService.addBookmarks(bookmarks), HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping(value = "/bookmark/{bookmarkID}/tag")
  @ResponseBody
  public ResponseEntity<Tag> addTag(
      @PathVariable(value = "bookmarkID") @NotNull Long bookmarkId,
      @RequestParam("tag") @Size(max = 50) @NotBlank String title) {
    final var bkmkOpt = bookmarkService.findById(bookmarkId);

    if (!bkmkOpt.isPresent()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var bookmark = bkmkOpt.get();

    Consumer<Tag> action =
        (t) -> {
          bookmarkService.addTagToBookmark(bookmark, t);
        };

    // Check if there is a tag by the given title.
    Tag tag;
    tag = tagService.findOrCreateTag(title);

    return new Response<Tag>(action, tag).get();
  }

  @PostMapping("/bookmark/{bookmarkID}/tagId")
  public ResponseEntity<BookmarkTagPair> addTagById(
      @PathVariable(value = "bookmarkID") Long bookmarkId,
      @RequestParam(value = "tagId") Long tagId) {

    var bookmark = bookmarkService.findById(bookmarkId);
    var tag = tagService.findById(tagId);

    Consumer<BookmarkTagPair> action =
        (BookmarkTagPair bt) -> {
          bookmarkService.addTagToBookmark(bt.bkmk(), bt.tag());
        };

    if (bookmark.isPresent() && tag.isPresent()) {
      var resp = new Response<BookmarkTagPair>();
      resp.prepareResponse(action, new BookmarkTagPair(bookmark.get(), tag.get()));
      return resp.get();
    }
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping(value = "bookmark/{bookmarkID}/tag")
  public ResponseEntity<Tag> deleteTagFromBookmark(
      @Valid @PathVariable("bookmarkID") long id, @RequestParam("tag") @NotBlank String title) {

    var t = tagService.getTagByTitle(title);
    var b = bookmarkService.findById(id);

    return b.isPresent()
        ? new Response<Tag>((tag) -> bookmarkService.deleteTag(id, tag), t).get()
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @DeleteMapping(value = "bookmark/{bookmarkID}/tagId", produces = "application/json")
  public ResponseEntity<Tag> deleteTagFromBookmarkById(
      @Valid @PathVariable("bookmarkID") long id, @RequestParam("tagId") @Valid long tagId) {

    var t = tagService.findById(tagId);
    var b = bookmarkService.findById(id);

    return b.isPresent()
        ? new Response<Tag>((tag) -> bookmarkService.deleteTag(id, tag), t).get()
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping(value = "bookmark/import", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Bookmark> importBookmarks(@RequestParam("file") MultipartFile file)
      throws IOException {

    var fBytes = file.getBytes();
    String string = new String(fBytes, StandardCharsets.UTF_8);
    System.out.println(string);
    var doc = Jsoup.parse(string);
    System.out.println(doc.title());

    // TODO: If the filename doesn't end with .html then throw.
    // var tmpUpload = Path.of("/tmp/" + file.getOriginalFilename());
    // Files.copy(file.getInputStream(), tmpUpload);
    // Files.read
    return bookmarkService.stream();
  }
}
