package dev.findfirst.core.repository;

import java.util.Optional;

import dev.findfirst.core.model.TagJDBC;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TagJDBCRepository extends CrudRepository<TagJDBC, Long> {
  @Query("SELECT t FROM Tag t WHERE t.tag_title = :title")
  Optional<TagJDBC> findByTitle(@Param("title") String title);
}
