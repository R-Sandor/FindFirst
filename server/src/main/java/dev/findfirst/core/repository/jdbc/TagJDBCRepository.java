package dev.findfirst.core.repository.jdbc;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.jdbc.TagJDBC;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TagJDBCRepository extends CrudRepository<TagJDBC, Long> {
  @Query("SELECT * FROM Tag t WHERE t.tag_title = :title AND t.tenant_id = :tenantId")
  Optional<TagJDBC> findByTitle(@Param("title") String title, @Param("tenantId") int tenantId);

  @Query("SELECT id FROM tag WHERE tag_title = :title AND tenant_id = :tenantId")
  Optional<Long> findIdByTitle(@Param("title") String title, @Param("tenantId") Integer tenantId);

  @Query("SELECT * FROM tag WHERE tag.tenant_id = :tenantId")
  List<TagJDBC> findAllByTenantId(@Param("tenantId") Integer tenantId);

  @Query("SELECT COUNT(*) FROM tag WHERE tenant_id = :tenantId")
  Integer countUserTags(@Param("tenantId") Integer tenantId);

  @Query("SELECT * FROM Tag t WHERE t.tenant_id = :tenantID AND t.tag_title IN :tagTitles ")
  List<TagDTO> findByTagTitles(@Param("tagTitles") List<String> tagTitles, @Param("tenantID") int tenantID);
}
