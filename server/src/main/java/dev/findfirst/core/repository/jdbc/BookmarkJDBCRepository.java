package dev.findfirst.core.repository.jdbc;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkJDBCRepository
    extends ListCrudRepository<BookmarkJDBC, Long>, PagingAndSortingRepository<BookmarkJDBC, Long> {

  @Query("SELECT * FROM bookmark where bookmark.url = :url AND bookmark.user_id = :userID")
  public Optional<BookmarkJDBC> findByUrl(@Param("url") String url, @Param("userID") int userID);

  @Query("SELECT * FROM bookmark where bookmark.user_id = :userID")
  public List<BookmarkJDBC> findAllBookmarksByUser(@Param("userID") int userID);

  public Page<BookmarkJDBC> findAllByUserId(int userId, Pageable pageable);

  @Query("SELECT * FROM Bookmark b WHERE b.screenshot_url IS NULL OR TRIM(b.screenshot_url)=''")
  List<BookmarkJDBC> findBookmarksWithEmptyOrBlankScreenShotUrl();

  @Query("select * from bookmark where to_tsvector(title) @@ to_tsquery(:keywords) AND bookmark.user_id = :userID")
  List<BookmarkJDBC> titleKeywordSearch(String keywords, int userID);

  @Query("""
      SELECT *
      FROM bookmark
      WHERE user_id = :userID AND id IN (
       SELECT bookmark_id
       FROM bookmark_tag
       WHERE bookmark_tag.tag_id IN (
         SELECT id FROM Tag t WHERE t.user_id = :userID AND LOWER(t.tag_title) IN (:tagTitles)
       )
      )
      """)
  List<BookmarkJDBC> findBookmarkByTagTitle(@Param("tagTitles") List<String> tagTitles,
      @Param("userID") int userID);
}
