package dev.findfirst.core.repository.jdbc;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.model.jdbc.TagJDBC;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TagJDBCRepository extends CrudRepository<TagJDBC, Long> {
  @Query("SELECT * FROM Tag t WHERE t.tag_title = :title AND t.user_id = :userID")
  Optional<TagJDBC> findByTitle(@Param("title") String title, @Param("userID") int userID);

  @Query("SELECT id FROM tag WHERE tag_title = :title AND user_id = :userID")
  Optional<Long> findIdByTitle(@Param("title") String title, @Param("userID") Integer userID);

  @Query("SELECT * FROM tag WHERE tag.user_id = :userID")
  List<TagJDBC> findAllByUserId(@Param("userID") Integer userID);

  @Query("SELECT COUNT(*) FROM tag WHERE user_id = :userID")
  Integer countUserTags(@Param("userID") Integer userID);

  @Query("SELECT * FROM Tag t WHERE t.user_id = :userID AND t.tag_title IN :tagTitles ")
  List<TagJDBC> findByTagTitles(@Param("tagTitles") List<String> tagTitles,
      @Param("userID") int userID);
}
