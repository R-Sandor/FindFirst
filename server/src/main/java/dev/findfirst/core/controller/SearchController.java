package dev.findfirst.core.controller;

import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.SearchBkmkByTagReq;
import dev.findfirst.core.model.SearchBkmkByTextReq;
import dev.findfirst.core.model.SearchBkmkByTitleReq;
import dev.findfirst.core.utilies.Response;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    @GetMapping("/api/search/tags")
    public ResponseEntity<List<Bookmark>> bookmarkSearchByTag(@Valid @RequestBody SearchBkmkByTagReq searchBkmkByTagReq) {
        // TODO finish
        return new Response<>(List.of(new Bookmark()), HttpStatus.OK).get();
    }

    @GetMapping("/api/search/title")
    public ResponseEntity<List<Bookmark>> bookMarkSearchByTitle(@Valid @RequestBody SearchBkmkByTitleReq searchBkmkByTitleReq) {
        // TODO finish
        return new Response<>(List.of(new Bookmark()), HttpStatus.OK).get();
    }

    @GetMapping("/api/search/text")
    public ResponseEntity<List<Bookmark>> bookMarkSearchByText(@Valid @RequestBody SearchBkmkByTextReq searchBkmkByTextReq) {
        // TODO finish
        return new Response<>(List.of(new Bookmark()), HttpStatus.OK).get();
    }
}
