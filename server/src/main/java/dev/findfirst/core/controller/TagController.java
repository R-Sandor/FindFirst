package dev.findfirst.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.findfirst.core.model.Tag;
import dev.findfirst.core.service.TagService;

@RestController
@RequestMapping("/api")
public class TagController {

  @Autowired
  TagService tagService;

  @GetMapping(value = "/tags")
  public ResponseEntity<List<Tag>> getTags() {
    return ResponseEntity.ok(tagService.getTags());
  }

  @PostMapping("/tags")
  public ResponseEntity<List<Tag>> addAllTags(@RequestBody String tags[]) {
    return ResponseEntity.ok(tagService.addAll(tags));
  }

  @DeleteMapping(value = "/tags")
  public ResponseEntity<List<Tag>> deleteAllTags() {
    return ResponseEntity.ok().body(tagService.deleteAllTags());
  }

  @PostMapping(value = "/tag")
  public ResponseEntity<Tag> addTag(@RequestParam("tag") String tag) {
    return ResponseEntity.ok(tagService.addTag(tag));
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
