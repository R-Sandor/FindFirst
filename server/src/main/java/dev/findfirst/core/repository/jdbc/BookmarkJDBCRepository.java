package dev.findfirst.core.repository.jdbc;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkJDBCRepository extends CrudRepository<BookmarkJDBC, Long> {

  @Query("SELECT * FROM bookmark where bookmark.url = :url AND bookmark.user_id = :userID")
  public Optional<BookmarkJDBC> findByUrl(@Param("url") String url,
      @Param("userID") int userID);

  @Query("SELECT * FROM bookmark where bookmark.user_id = :userID")
  public List<BookmarkJDBC> findAllBookmarksByUser(@Param("userID") int userID);


  @Query("SELECT b FROM Bookmark b WHERE b.screenshotUrl IS NULL OR TRIM(b.screenshotUrl)=''")
  List<BookmarkJDBC> findBookmarksWithEmptyOrBlankScreenShotUrl();

}
