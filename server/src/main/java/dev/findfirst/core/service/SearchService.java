package dev.findfirst.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import dev.findfirst.core.dto.BookmarkOnly;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.jdbc.TagJDBC;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;
import dev.findfirst.core.repository.jdbc.TagJDBCRepository;
import dev.findfirst.security.userauth.context.UserContext;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final TagJDBCRepository tagRepository;

  private final BookmarkJDBCRepository bookmarkRepo;

  private final BookmarkService bookmarkService;

  private final UserContext userContext;

  public List<BookmarkOnly> titleKeywordSearch(String[] keywords) {
    StringJoiner joiner = new StringJoiner(" | ");
    for (String kw : keywords) {
      joiner.add(kw);
    }
    return bookmarkService
        .convertBookmarkJDBCToBookmarkOnly(bookmarkRepo.titleKeywordSearch(joiner.toString(), userContext.getUserId()));
  }

  public List<BookmarkOnly> bookmarkSearchByTagTitles(List<String> titles) {
    List<TagJDBC> foundTags = tagRepository.findByTagTitles(titles, userContext.getUserId());
    List<BookmarkOnly> foundBookmarks = new ArrayList<>();
    return foundBookmarks;
  }

}
