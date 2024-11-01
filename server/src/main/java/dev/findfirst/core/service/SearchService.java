package dev.findfirst.core.service;

import java.util.ArrayList;
import java.util.List;

import dev.findfirst.core.model.jpa.Bookmark;
import dev.findfirst.core.model.jpa.Tag;
import dev.findfirst.core.repository.jpa.TagRepository;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

  private final TagRepository tagRepository;

  public SearchService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public List<Bookmark> bookmarkSearchByTagTitles(List<String> titles) {
    List<Tag> foundTags = tagRepository.findByTagTitles(titles);
    List<Bookmark> foundBookmarks = new ArrayList<>();
    for (Tag foundTag : foundTags) {
      foundBookmarks.addAll(foundTag.getBookmarks());
    }
    return foundBookmarks;
  }

}
