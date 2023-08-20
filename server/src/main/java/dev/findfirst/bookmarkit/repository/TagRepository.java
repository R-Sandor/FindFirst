package dev.findfirst.bookmarkit.repository;

import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.security.tenant.repository.TenantableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TagRepository extends TenantableRepository<Tag>, TagRepoCustom {
  @Query("SELECT t FROM Tag t WHERE t.tag_title =?1")
  Tag findByTagTitle(String title);

  //   @Query(
  //   "SELECT new dev.findfirst.bookmarkit.model.TagCntRecord(" +
  //   "new dev.findfirst.bookmarkit.model.Tag(tag.id, tag.title, tag.url), COUNT(tag.id))" +
  //   "FROM tag JOIN bookmark ON bookmark.tag_id" +
  //   "GROUP BY tag.id"
  // )
  // List<TagCntRecord> test();
}
