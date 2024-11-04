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

  @Query("SELECT bt.tag_id from bookmark_tag bt join bookmark bk ON bk.id = bt.bookmark_id WHERE bk.tenant_id = :tenantId")
  List<Integer> getUserAllTagIdsToBookmarks(@Param(value = "tenantId") Integer tenantId);

  @Query("""
      SELECT bt.tag_id, bt.bookmark_id from bookmark_tag bt
        join bookmark bk ON bk.id = bt.bookmark_id WHERE bk.tenant_id = :tenantId
      AND bk.id = :bkId
      """)
  Set<BookmarkTag> getAllTagIdsForBookmark(@Param("bkId") Long bookmark,
      @Param("tenantId") Integer tenantId);

  @Query("""
      SELECT bt.bookmark_id from bookmark_tag bt
        join tag tg ON tg.id = bt.tag_id WHERE tg.tenant_id = :tenantId
      AND tg.id = :tgId
      """)
  List<Long> getAllBookmarkIdsForTag(@Param("tgId") Long tag, @Param("tenantId") Integer tenantId);
}
