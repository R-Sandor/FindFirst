package dev.findfirst.core.repository;

import java.util.List;

import dev.findfirst.core.model.BookmarkTag;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkTagRepository extends CrudRepository<BookmarkTag, Long> {
  List<BookmarkTag> findByBookmarkId(Long bookmarkId);

  List<BookmarkTag> findByTagId(Long tagId);

  @Query("SELECT bt.tag_id from bookmark_tag bt join bookmark bk ON bk.id = bt.bookmark_id WHERE bk.tenant_id = :tenantId")
  List<Integer> getUserAllTagIdsToBookmarks(@Param(value = "tenantId") Integer tenantId);

  @Query("""
      SELECT bt.tag_id from bookmark_tag bt
        join bookmark bk ON bk.id = bt.bookmark_id WHERE bk.tenant_id = :tenantId
      AND bk.id = :bkId
      """)
  List<Integer> getAllTagIdsForBookmark(@Param("bkId") Integer bookmark,
      @Param(value = "tenantId") Integer tenantId);

}
