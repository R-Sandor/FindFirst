package dev.findfirst.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import dev.findfirst.core.dto.BookmarkOnly;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;
import dev.findfirst.security.userauth.context.UserContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

  private final BookmarkJDBCRepository bookmarkRepo;

  private final BookmarkService bookmarkService;

  private final UserContext userContext;

  public List<BookmarkOnly> titleKeyword(String[] keywords) {
    StringJoiner joiner = new StringJoiner(" | ");
    for (String kw : keywords) {
      joiner.add(kw);
    }
    return bookmarkService.convertBookmarkJDBCToBookmarkOnly(
        bookmarkRepo.titleKeywordSearch(joiner.toString(), userContext.getUserId()));
  }

  public List<BookmarkOnly> bookmarksByTags(String[] tags) {
    return bookmarkService.convertBookmarkJDBCToBookmarkOnly(
        bookmarkRepo.findBookmarkByTagTitle(Arrays.asList(tags), userContext.getUserId()));
  }

}
