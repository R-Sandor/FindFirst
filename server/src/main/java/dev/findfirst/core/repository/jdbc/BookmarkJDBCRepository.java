package dev.findfirst.core.repository.jdbc;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkJDBCRepository extends CrudRepository<BookmarkJDBC, Long> {

  @Query("SELECT * FROM bookmark where bookmark.url = :url AND bookmark.tenant_id = :tenantID")
  public Optional<BookmarkJDBC> findByUrl(@Param("url") String url, @Param("tenantID") int tenantID);

  @Query("SELECT * FROM bookmark where bookmark.tenant_id = :tenantID")
  public List<BookmarkJDBC> findAllBookmarksByUser(@Param("tenantID") int tenantID);


  @Query("SELECT b FROM Bookmark b WHERE b.screenshotUrl IS NULL OR TRIM(b.screenshotUrl)=''")
  List<BookmarkJDBC> findBookmarksWithEmptyOrBlankScreenShotUrl();

}
