package dev.findfirst.core.repository.jdbc;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.jdbc.TagJDBC;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TagJDBCRepository extends CrudRepository<TagJDBC, Long> {
  @Query("SELECT * FROM Tag t WHERE t.tag_title = :title AND t.user_id = :userId")
  Optional<TagJDBC> findByTitle(@Param("title") String title, @Param("userId") int userId);

  @Query("SELECT id FROM tag WHERE tag_title = :title AND user_id = :userId")
  Optional<Long> findIdByTitle(@Param("title") String title, @Param("userId") Integer userId);

  @Query("SELECT * FROM tag WHERE tag.user_id = :userId")
  List<TagJDBC> findAllByUserId(@Param("userId") Integer userId);

  @Query("SELECT COUNT(*) FROM tag WHERE user_id = :userId")
  Integer countUserTags(@Param("userId") Integer userId);

  @Query("SELECT * FROM Tag t WHERE t.user_id = :userId AND t.tag_title IN :tagTitles ")
  List<TagDTO> findByTagTitles(@Param("tagTitles") List<String> tagTitles,
      @Param("tenantID") int userId);
}
