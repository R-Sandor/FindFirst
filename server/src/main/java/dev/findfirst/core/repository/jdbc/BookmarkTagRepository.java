package dev.findfirst.core.repository.jdbc;

import java.util.List;
import java.util.Set;

import dev.findfirst.core.model.jdbc.BookmarkTag;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkTagRepository
    extends CrudRepository<BookmarkTag, Long>, BookmarkTagRepositoryCustom {
  List<BookmarkTag> findByBookmarkId(Long bookmarkId);

  List<BookmarkTag> findByTagId(Long tagId);

  @Query("SELECT bt.tag_id from bookmark_tag bt join bookmark bk ON bk.id = bt.bookmark_id WHERE bk.user_id = :userId")
  List<Integer> getUserAllTagIdsToBookmarks(@Param(value = "userId") Integer userId);

  @Query("""
      SELECT bt.tag_id, bt.bookmark_id from bookmark_tag bt
        join bookmark bk ON bk.id = bt.bookmark_id WHERE bk.user_id = :userId
      AND bk.id = :bkId
      """)
  Set<BookmarkTag> getAllTagIdsForBookmark(@Param("bkId") Long bookmark,
      @Param("userId") Integer userId);

  @Query("""
      SELECT bt.bookmark_id from bookmark_tag bt
        join tag tg ON tg.id = bt.tag_id WHERE tg.user_id = :userId
      AND tg.id = :tgId
      """)
  List<Long> getAllBookmarkIdsForTag(@Param("tgId") Long tag, @Param("userId") Integer userId);
}
