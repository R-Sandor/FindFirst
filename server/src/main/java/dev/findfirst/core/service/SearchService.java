package dev.findfirst.core.service;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;
import dev.findfirst.core.service.TypesenseService.SearchHighlightResult;
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

  private final TypesenseService typesense;

  public List<BookmarkDTO> titleKeyword(String[] keywords) {
    StringJoiner joiner = new StringJoiner(" | ");
    for (String kw : keywords) {
      joiner.add(kw);
    }
    var userID = userContext.getUserId();
    return bookmarkService.convertBookmarkJDBCToDTO(
        bookmarkRepo.titleKeywordSearch(joiner.toString(), userID), userID);
  }

  public List<BookmarkDTO> bookmarksByTags(List<String> tags) {
    var userID = userContext.getUserId();
    return bookmarkService.convertBookmarkJDBCToDTO(bookmarkRepo
        .findBookmarkByTagTitle(tags.stream().map(String::toLowerCase).toList(), userID), userID);
  }

  public List<BookmarkDTO> bookmarksByText(String text) {
    var userID = userContext.getUserId();
    List<SearchHighlightResult> searchHighlightResults = typesense.search(text);

    record IdHighlight(List<Long> ids, List<String> hightlights) {
    }

    IdHighlight ih = searchHighlightResults.stream()
        .collect(Collectors.teeing(Collectors.mapping(hit -> hit.id(), Collectors.toList()),
            Collectors.mapping(hit -> hit.highlight(), Collectors.toList()),
            IdHighlight::new));

    var bookmarks = bookmarkRepo.findAllById(ih.ids());
    return bookmarkService.convertBookmarkJDBCToDTO(bookmarks, ih.hightlights(), userID);

  }

}
