package dev.findfirst.core.repository.jdbc;

import dev.findfirst.core.model.jdbc.BookmarkTag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookmarkTagRepositoryCustomImpl implements BookmarkTagRepositoryCustom {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public int update(BookmarkTag bookmarkTag) {
    String sql =
        "UPDATE bookmark_tag SET bookmark_id = ?, tag_id = ? WHERE bookmark_id = ? AND tag_id = ?";
    return jdbcTemplate.update(sql, bookmarkTag.getBookmarkId(), bookmarkTag.getTagId(),
        bookmarkTag.getBookmarkId(), bookmarkTag.getTagId());
  }
}
