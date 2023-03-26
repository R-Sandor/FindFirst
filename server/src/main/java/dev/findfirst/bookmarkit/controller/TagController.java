package dev.findfirst.bookmarkit.controller;

import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.service.TagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {

  @Autowired TagService tagService;

  @PostMapping(value = "/tag/addTag/{tag}")
  public void addTag(@PathVariable Tag tag) {
    tagService.addTag(tag);
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
}
