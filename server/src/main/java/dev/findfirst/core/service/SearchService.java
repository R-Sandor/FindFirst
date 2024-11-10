package dev.findfirst.core.service;

import java.util.ArrayList;
import java.util.List;

import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.repository.jdbc.TagJDBCRepository;
import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

  private final TagJDBCRepository tagRepository;

  private final TenantContext tenantContext;

  public SearchService(TagJDBCRepository tagRepository, TenantContext tenantContext) {
    this.tagRepository = tagRepository;
    this.tenantContext = tenantContext;
  }

  public List<BookmarkDTO> bookmarkSearchByTagTitles(List<String> titles) {
    List<TagDTO> foundTags = tagRepository.findByTagTitles(titles, tenantContext.getTenantId());
    List<BookmarkDTO> foundBookmarks = new ArrayList<>();
    for (TagDTO foundTag : foundTags) {
      foundBookmarks.addAll(foundTag.bookmarks());
    }
    return foundBookmarks;
  }

}
