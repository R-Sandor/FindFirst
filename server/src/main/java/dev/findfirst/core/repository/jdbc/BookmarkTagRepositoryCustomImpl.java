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
    String sql = "INSERT INTO bookmark_tag VALUES(?, ?)";
    return jdbcTemplate.update(sql, bookmarkTag.getBookmarkId(), bookmarkTag.getTagId());
  }
}
