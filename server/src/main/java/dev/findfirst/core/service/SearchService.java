package dev.findfirst.core.service;

import java.util.ArrayList;
import java.util.List;

import dev.findfirst.core.dto.BookmarkOnly;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.repository.jdbc.TagJDBCRepository;
import dev.findfirst.security.userAuth.context.UserContext;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

  private final TagJDBCRepository tagRepository;

  private final UserContext userContext;

  public SearchService(TagJDBCRepository tagRepository, UserContext userContext) {
    this.tagRepository = tagRepository;
    this.userContext = userContext;
  }

  public List<BookmarkOnly> bookmarkSearchByTagTitles(List<String> titles) {
    List<TagDTO> foundTags = tagRepository.findByTagTitles(titles, userContext.getUserId());
    List<BookmarkOnly> foundBookmarks = new ArrayList<>();
    for (TagDTO foundTag : foundTags) {
      foundBookmarks.addAll(foundTag.bookmarks());
    }
    return foundBookmarks;
  }

}
