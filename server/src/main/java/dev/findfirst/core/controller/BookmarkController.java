package dev.findfirst.core.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import dev.findfirst.core.dto.AddBkmkReq;
import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.jdbc.BookmarkTag;
import dev.findfirst.core.service.BookmarkService;
import dev.findfirst.core.service.TagService;
import dev.findfirst.core.utilies.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class BookmarkController {

  private final BookmarkService bookmarkService;

  private final TagService tagService;

  @GetMapping("/bookmarks")
  public ResponseEntity<List<BookmarkDTO>> getAllBookmarks() {
    return new Response<List<BookmarkDTO>>(bookmarkService.listJDBC(), HttpStatus.OK).get();
  }

  @GetMapping(value = "/bookmarks/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> exportAllBookmarks() {
    var exported = bookmarkService.export();
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=findfirst-bookmarks.html")
        .body(exported.getBytes());
  }

  @DeleteMapping(value = "/bookmarks")
  public ResponseEntity<String> deleteAll() {
    bookmarkService.deleteAllBookmarks();
    return ResponseEntity.ok("Deleted All user's bookmarks");
  }

  @GetMapping(value = "/bookmark")
  public ResponseEntity<BookmarkDTO> getBookmarkById(@RequestParam("id") long id) {
    return new Response<BookmarkDTO>(bookmarkService.getBookmarkDTOById(id)).get();
  }

  @PostMapping(value = "/bookmark")
  public ResponseEntity<BookmarkDTO> addBookmark(@RequestBody AddBkmkReq req) {
    var response = new Response<BookmarkDTO>();
    try {
      BookmarkDTO createdBookmark = bookmarkService.addBookmark(req);
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
  public ResponseEntity<List<BookmarkDTO>> addBookmarks(
      @RequestBody @Size(min = 1, max = 100) List<AddBkmkReq> bookmarks) {
    try {
      return new ResponseEntity<List<BookmarkDTO>>(bookmarkService.addBookmarks(bookmarks),
          HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping(value = "/bookmark/{bookmarkID}/tag")
  @ResponseBody
  public ResponseEntity<TagDTO> addTag(@PathVariable(value = "bookmarkID") @NotNull long bookmarkId,
      @RequestParam("tag") @Size(max = 512) @NotBlank String title) {
    final var bkmkOpt = bookmarkService.findByIdJDBC(bookmarkId);

    if (!bkmkOpt.isPresent()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var bookmark = bkmkOpt.get();

    // Check if there is a tag by the given title.
    TagDTO tagDTO = tagService.findOrCreateTag(title);

    var tag = tagService.findByIdJDBC(tagDTO.id()).orElseThrow();

    bookmarkService.addTagToBookmarkJDBC(bookmark, tag);
    return ResponseEntity.ofNullable(tagDTO);

  }

  @PostMapping("/bookmark/{bookmarkID}/tagId")
  public ResponseEntity<BookmarkDTO> addTagById(@PathVariable(value = "bookmarkID") Long bookmarkId,
      @RequestParam(value = "tagId") Long tagId) {

    log.debug("Getting Bookmark");
    var bookmark = bookmarkService.findByIdJDBC(bookmarkId);
    log.debug("Getting Tag: {}", tagId);
    var tag = tagService.findByIdJDBC(tagId);

    log.debug(bookmark.orElseThrow().toString());
    log.debug(tag.orElseThrow().toString());

    if (bookmark.isPresent() && tag.isPresent()) {
      var bk = bookmark.get();
      return ResponseEntity.ok(bookmarkService.addTagById(bk, tagId));
    }
    return ResponseEntity.badRequest().build();
  }

  @DeleteMapping(value = "bookmark/{bookmarkID}/tag")
  public ResponseEntity<BookmarkTag> deleteTagFromBookmark(
      @Valid @PathVariable("bookmarkID") long bookmarkID,
      @RequestParam("tag") @NotBlank String title) {

    var t = tagService.findIdByTagTitleJDBC(title);
    var b = bookmarkService.findByIdJDBC(bookmarkID);

    return b.isPresent() && t.isPresent()
        ? new ResponseEntity<BookmarkTag>(
            bookmarkService.deleteTag(new BookmarkTag(bookmarkID, t.get())), HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @DeleteMapping(value = "bookmark/{bookmarkID}/tagId", produces = "application/json")
  public ResponseEntity<BookmarkTag> deleteTagFromBookmarkById(
      @Valid @PathVariable("bookmarkID") long bookmarkID,
      @RequestParam("tagId") @Valid long tagId) {

    var t = tagService.findByIdJDBC(tagId);
    var b = bookmarkService.findByIdJDBC(bookmarkID);

    return b.isPresent() && t.isPresent()
        ? new ResponseEntity<BookmarkTag>(
            bookmarkService.deleteTag(new BookmarkTag(bookmarkID, t.get().getId())), HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping(value = "bookmark/import", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<BookmarkDTO> importBookmarks(@RequestParam("file") MultipartFile file)
      throws IOException {

    var fBytes = file.getBytes();
    String docStr = new String(fBytes, StandardCharsets.UTF_8);

    // TODO: If the filename doesn't end with .html then throw.
    // TODO: Check if the file is too big!

    return bookmarkService.importBookmarks(docStr);
  }
}
