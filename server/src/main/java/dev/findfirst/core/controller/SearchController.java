package dev.findfirst.core.controller;

import java.util.List;

import jakarta.validation.Valid;

import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.model.SearchBkmkByTagReq;
import dev.findfirst.core.model.SearchBkmkByTextReq;
import dev.findfirst.core.model.SearchBkmkByTitleReq;
import dev.findfirst.core.utilies.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

  @GetMapping("/api/search/tags")
  public ResponseEntity<List<BookmarkDTO>> bookmarkSearchByTag(
      @Valid @RequestBody SearchBkmkByTagReq searchBkmkByTagReq) {
    // TODO finish
    return new Response<>(List.of(new BookmarkDTO(0, null, null, null, false, null, null, null)),
        HttpStatus.OK).get();
  }

  @GetMapping("/api/search/title")
  public ResponseEntity<List<BookmarkDTO>> bookMarkSearchByTitle(
      @Valid @RequestBody SearchBkmkByTitleReq searchBkmkByTitleReq) {
    // TODO finish
    return new Response<>(List.of(new BookmarkDTO(0, null, null, null, false, null, null, null)),
        HttpStatus.OK).get();
  }

  @GetMapping("/api/search/text")
  public ResponseEntity<List<BookmarkDTO>> bookMarkSearchByText(
      @Valid @RequestBody SearchBkmkByTextReq searchBkmkByTextReq) {
    // TODO finish
    return new Response<>(List.of(new BookmarkDTO(0, null, null, null, false, null, null, null)),
        HttpStatus.OK).get();
  }
}
