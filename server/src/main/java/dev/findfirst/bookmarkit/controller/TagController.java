package dev.findfirst.bookmarkit.controller;

import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.model.TagCntRecord;
import dev.findfirst.bookmarkit.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TagController {

  @Autowired TagService tagService;

  @PostMapping(value = "/tag/addTag/{tag}")
  public void addTag(@PathVariable Tag tag) {
    tagService.addTag(tag);
  }

  /**
   * Get all the tags associated to a Bookmark.
   * @param id of the bookmark to retrieve tags.
   * @return 
   */
  @GetMapping("/tag/bkmk/{id}")
  public ResponseEntity<Object> getTagByBookmarkId(@PathVariable Long id) { 
    if (id == null) { 
      return new ResponseEntity<>("Please provide an id", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    var tags = tagService.getTagsByBookmarkId(id);
    return new ResponseEntity<Object>(tags, null, HttpStatus.OK);
  }

  @PostMapping(value = "/tag/addTags/{tags}")
  public void addTags(List<Tag> tags) {
    tagService.addAll(tags);
  }

  @PostMapping(value = "/tag/deleteAll")
  public void deleteAllTags() {
    tagService.deleteAllTags();
  }

  @GetMapping(value = "/tags")
  public List<Tag> getTags() {
    return tagService.getTags();
  }

  @GetMapping(value = "/tagscnt")
  public List<TagCntRecord> getTagsWithCnt() {
    return tagService.getTagsWithCnt();
  }
}
