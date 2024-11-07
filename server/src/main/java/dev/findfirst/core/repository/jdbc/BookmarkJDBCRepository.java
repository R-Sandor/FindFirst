package dev.findfirst.core.repository.jdbc;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookmarkJDBCRepository extends CrudRepository<BookmarkJDBC, Long> {

  @Query("SELECT * FROM bookmark where bookmark.url = :url AND bookmark.tenant_id = :tenantID")
  public Optional<BookmarkJDBC> findByUrl(@Param("url") String url, @Param("tenantID") int tenantID);
}
