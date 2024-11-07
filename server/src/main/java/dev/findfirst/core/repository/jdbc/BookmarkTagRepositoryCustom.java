package dev.findfirst.core.repository.jdbc;

import dev.findfirst.core.model.jdbc.BookmarkTag;

public interface BookmarkTagRepositoryCustom {
  int saveBookmarkTag(BookmarkTag bookmarkTag);

  int deleteBookmarkTag(BookmarkTag bookmarkTag);
}
