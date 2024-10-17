package dev.findfirst.core.service;

import java.util.ArrayList;
import java.util.List;

import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.repository.TagRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

  private final TagRepository tagRepository;

  @Autowired
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
