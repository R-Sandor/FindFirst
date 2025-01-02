package dev.findfirst.core.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import dev.findfirst.core.dto.AddBkmkReq;
import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.dto.UpdateBookmarkReq;
import dev.findfirst.core.exceptions.BookmarkNotFoundException;
import dev.findfirst.core.exceptions.TagNotFoundException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BookmarkController
{

  private final BookmarkService bookmarkService;

  private final TagService tagService;

  @GetMapping("/bookmarks")
  public ResponseEntity<List<BookmarkDTO>> getAllBookmarks()
  {
    return new Response<>(bookmarkService.listJDBC(), HttpStatus.OK).get();
  }

  @GetMapping(value = "/bookmarks/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> exportAllBookmarks()
  {
    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=findfirst-bookmarks.html")
            .body(bookmarkService.export().getBytes());
  }

  @DeleteMapping(value = "/bookmarks")
  public ResponseEntity<String> deleteAll()
  {
    bookmarkService.deleteAllBookmarks();
    return ResponseEntity.ok("Deleted All user's bookmarks");
  }

  @GetMapping(value = "/bookmark")
  public ResponseEntity<BookmarkDTO> getBookmarkById(@RequestParam("id") long id)
  {
    return new Response<>(bookmarkService.getBookmarkDTOById(id)).get();
  }

  @ExceptionHandler(TagNotFoundException.class)
  public ResponseEntity<String> handle()
  {
    return ResponseEntity.internalServerError().body(new TagNotFoundException().getMessage());
  }

  @PostMapping("/bookmark")
  public ResponseEntity<BookmarkDTO> addBookmark(@RequestBody AddBkmkReq req)
          throws TagNotFoundException, URISyntaxException
  {
    var response = new Response<BookmarkDTO>();
    BookmarkDTO createdBookmark = bookmarkService.addBookmark(req);
    return response.setResponse(createdBookmark, HttpStatus.OK);
  }

  /**
   * Partial update to a bookmark.
   *
   * @param updateBookmarkReq contains all the params accepted; The id, and title are required
   *                          fields.
   * @throws BookmarkNotFoundException
   */
  @PatchMapping("/bookmark")
  public ResponseEntity<BookmarkDTO> updateBookmark(
          @Valid @RequestBody UpdateBookmarkReq updateBookmarkReq) throws BookmarkNotFoundException
  {
    return new ResponseEntity<>(bookmarkService.updateBookmark(updateBookmarkReq), HttpStatus.OK);
  }

  @DeleteMapping("/bookmark")
  public ResponseEntity<String> deleteById(@RequestParam("id") Long id)
          throws JsonProcessingException
  {
    bookmarkService.deleteBookmark(id);
    ObjectMapper mapper = new ObjectMapper();
    String jsonStr = mapper.writeValueAsString("Deleted Bookmark %s".formatted(id));
    return new ResponseEntity<>(jsonStr, HttpStatus.OK);
  }

  @PostMapping("/bookmark/addBookmarks")
  public ResponseEntity<List<BookmarkDTO>> addBookmarks(
          @RequestBody @Size(min = 1, max = 100) List<AddBkmkReq> bookmarks)
  {
    return new ResponseEntity<>(bookmarkService.addBookmarks(bookmarks), HttpStatus.OK);
  }

  @PostMapping("/bookmark/{bookmarkID}/tag")
  public ResponseEntity<TagDTO> addTag(@PathVariable(value = "bookmarkID") @NotNull long bookmarkId,
                                       @RequestParam("tag") @Size(max = 512) @NotBlank String title)
          throws BookmarkNotFoundException
  {
    final var bkmkOpt = bookmarkService.findByIdJDBC(bookmarkId);

    var bookmark = bkmkOpt.orElseThrow(BookmarkNotFoundException::new);

    // Check if there is a tag by the given title.
    TagDTO tagDTO = tagService.findOrCreateTag(title);

    var tag = tagService.findByIdJDBC(tagDTO.id()).orElseThrow();

    bookmarkService.addTagToBookmarkJDBC(bookmark, tag);
    return ResponseEntity.ofNullable(tagDTO);

  }

  @PostMapping("/bookmark/{bookmarkID}/tagId")
  public ResponseEntity<BookmarkDTO> addTagById(@PathVariable(value = "bookmarkID") Long bookmarkId,
                                                @RequestParam(value = "tagId") Long tagId)
          throws BookmarkNotFoundException, TagNotFoundException
  {

    var bookmark =
            bookmarkService.findByIdJDBC(bookmarkId).orElseThrow(BookmarkNotFoundException::new);
    var tag = tagService.findByIdJDBC(tagId).orElseThrow(TagNotFoundException::new);

    return ResponseEntity.ok(bookmarkService.addTagById(bookmark, tag.getId()));
  }

  @ExceptionHandler(BookmarkNotFoundException.class)
  public ResponseEntity<String> handleNotFoundException()
  {
    return ResponseEntity.internalServerError().body(new BookmarkNotFoundException().getMessage());
  }

  @DeleteMapping("bookmark/{bookmarkID}/tag")
  public ResponseEntity<BookmarkTag> deleteTagFromBookmark(
          @Valid @PathVariable("bookmarkID") long bookmarkID,
          @RequestParam("tag") @NotBlank String title)
          throws TagNotFoundException, BookmarkNotFoundException
  {

    var t = tagService.findIdByTagTitleJDBC(title).orElseThrow(TagNotFoundException::new);
    // verify that the bookmark exists.
    var b = bookmarkService.findByIdJDBC(bookmarkID).orElseThrow(BookmarkNotFoundException::new);

    return new ResponseEntity<>(bookmarkService.deleteTag(new BookmarkTag(b.getId(), t)),
            HttpStatus.OK);
  }

  @DeleteMapping("bookmark/{bookmarkID}/tagId")
  public ResponseEntity<BookmarkTag> deleteTagFromBookmarkById(
          @Valid @PathVariable("bookmarkID") long bookmarkID, @RequestParam("tagId") @Valid long tagId)
          throws TagNotFoundException, BookmarkNotFoundException
  {

    var t = tagService.findByIdJDBC(tagId).orElseThrow(TagNotFoundException::new);
    // verify that the bookmark exists.
    var b = bookmarkService.findByIdJDBC(bookmarkID).orElseThrow(BookmarkNotFoundException::new);

    return new ResponseEntity<>(bookmarkService.deleteTag(new BookmarkTag(b.getId(), t.getId())),
            HttpStatus.OK);
  }

  @PostMapping(value = "bookmark/import", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<BookmarkDTO> importBookmarks(@RequestParam("file") MultipartFile file)
          throws IOException
  {
    // Check file size
    final long MAX_FILE_SIZE = 250L * 1_000_000 / 8; // 250 Mb
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
              "File too large. Maximum allowed size is 250MB");
    }

    // Check file extension
    String originalName = file.getOriginalFilename();
    if (originalName == null || !originalName.toLowerCase().endsWith(".html")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              "Uploaded file must have .html extension");
    }

    // Read file content
    var fBytes = file.getBytes();
    String docStr = new String(fBytes, StandardCharsets.UTF_8);

    return bookmarkService.importBookmarks(docStr);
  }
}