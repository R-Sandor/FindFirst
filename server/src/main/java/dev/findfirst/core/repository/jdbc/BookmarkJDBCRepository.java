package dev.findfirst.core.repository.jdbc;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkJDBCRepository extends CrudRepository<BookmarkJDBC, Long> {

  @Query("SELECT * FROM bookmark where bookmark.url = :url AND bookmark.user_id = :userID")
  public Optional<BookmarkJDBC> findByUrl(@Param("url") String url, @Param("userID") int userID);

  @Query("SELECT * FROM bookmark where bookmark.user_id = :userID")
  public List<BookmarkJDBC> findAllBookmarksByUser(@Param("userID") int userID);


  @Query("SELECT b FROM Bookmark b WHERE b.screenshotUrl IS NULL OR TRIM(b.screenshotUrl)=''")
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
