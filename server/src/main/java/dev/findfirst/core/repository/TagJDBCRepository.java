package dev.findfirst.core.repository;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.model.TagJDBC;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TagJDBCRepository extends CrudRepository<TagJDBC, Long> {
  @Query("SELECT t FROM Tag t WHERE t.tag_title = :title AND t.tenant_id = :tenantId")
  Optional<TagJDBC> findByTitle(@Param("title") String title, @Param("tenantId") int tenantId);

  @Query("SELECT * FROM tag WHERE tag.tenant_id = :tenantId")
  List<TagJDBC> findAllByTenantId(@Param("tenantId") Integer tenantId);

  @Query("SELECT COUNT(*) FROM tag WHERE tenant_id = :tenantId")
  Integer countUserTags(@Param("tenantId") Integer tenantId);
}
