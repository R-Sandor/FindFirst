package dev.findfirst.core.controller;

import java.util.List;

import jakarta.validation.Valid;

import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.dto.BookmarkOnly;
import dev.findfirst.core.model.SearchBkmkByTagReq;
import dev.findfirst.core.model.SearchBkmkByTextReq;
import dev.findfirst.core.model.SearchBkmkByTitleReq;
import dev.findfirst.core.service.SearchService;
import dev.findfirst.core.utilies.Response;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

  private final SearchService search;


  @GetMapping("/api/search/tags")
  public ResponseEntity<List<BookmarkDTO>> searchBookmarkByTags(
      @Valid @ModelAttribute SearchBkmkByTagReq searchBkmkByTagReq) {
    return ResponseEntity.ok(search.bookmarksByTags(searchBkmkByTagReq.tags()));
  }

  @GetMapping("/api/search/title")
  public ResponseEntity<List<BookmarkDTO>> searchBookmarksByTitleKeywords(
      @Valid @ModelAttribute SearchBkmkByTitleReq searchBkmkByTitleReq) {
    return new Response<>(search.titleKeyword(searchBkmkByTitleReq.keywords()), HttpStatus.OK)
        .get();
  }

  /**
   * This is BLOCKED by requiring Issue #281 The search engine is required to do the text search.
   */
  @GetMapping("/api/search/text")
  public ResponseEntity<List<BookmarkDTO>> searchBookmarkByText(
      @Valid @ModelAttribute SearchBkmkByTextReq searchBkmkByTextReq) {
    return new Response<>(List.of(new BookmarkDTO(0, null, null, null, false, null, null, null)),
        HttpStatus.OK).get();
  }
}
