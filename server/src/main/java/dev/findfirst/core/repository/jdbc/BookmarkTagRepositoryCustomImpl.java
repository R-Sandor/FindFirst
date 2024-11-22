package dev.findfirst.core.repository.jdbc;

import dev.findfirst.core.model.jdbc.BookmarkTag;
import dev.findfirst.security.userauth.context.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkTagRepositoryCustomImpl implements BookmarkTagRepositoryCustom {
  private final JdbcTemplate jdbcTemplate;

  private final UserContext uContext;

  @Override
  public int saveBookmarkTag(BookmarkTag bookmarkTag) {
    String sql = "INSERT INTO bookmark_tag VALUES(?, ?)";
    return jdbcTemplate.update(sql, bookmarkTag.getBookmarkId(), bookmarkTag.getTagId());
  }

  @Override
  public int deleteBookmarkTag(BookmarkTag bookmarkTag) {
    String sql = "DELETE FROM bookmark_tag bt USING bookmark WHERE bt.bookmark_id = ? AND bt.tag_id = ? AND bookmark.user_id = ?";
    return jdbcTemplate.update(sql, bookmarkTag.getBookmarkId(), bookmarkTag.getTagId(),
        uContext.getUserId());
  }

  @Override
  public int deleteAllTagsByUser() {
    String sql = "DELETE FROM bookmark_tag bt USING tag WHERE bt.tag_id = tag.id AND tag.user_id = ?";
    return jdbcTemplate.update(sql, uContext.getUserId());
  }
}
