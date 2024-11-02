package dev.findfirst.core.controller;

import java.util.List;

import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.jdbc.TagJDBC;
import dev.findfirst.core.model.jpa.Tag;
import dev.findfirst.core.service.TagService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  // JDBC in progress 
  @GetMapping(value = "/tags")
  public ResponseEntity<List<TagDTO>> getTags() {
    return ResponseEntity.ok(tagService.getTags());
  }

  @PostMapping("/tags")
  public ResponseEntity<List<TagDTO>> addAllTags(@RequestBody String tags[]) {
    return ResponseEntity.ok().body(tagService.addAll(tags));
  }

  @DeleteMapping(value = "/tags")
  public ResponseEntity<List<Tag>> deleteAllTags() {
    return ResponseEntity.ok().body(tagService.deleteAllTags());
  }

  // JDBC 
  @PostMapping(value = "/tag")
  public ResponseEntity<TagJDBC> addTag(@RequestParam("tag") String tag) {
    return ResponseEntity.ok(tagService.findOrCreateTagJDBC(tag));
  }

  /**
   * Get all the tags associated to a Bookmark.
   *
   * @param id of the bookmark to retrieve tags.
   * @return
   */
  @GetMapping("/tag/bkmk")
  public ResponseEntity<List<Tag>> getTagByBookmarkId(
      @RequestParam("bookmarkId") @NonNull Long id) {
    return ResponseEntity.ok(tagService.getTagsByBookmarkId(id));
  }
}
