package dev.findfirst.core.repository.jpa;

import java.util.List;
import java.util.Optional;

import dev.findfirst.core.model.jpa.Tag;
import dev.findfirst.security.userAuth.tenant.repository.TenantableRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TagRepository extends TenantableRepository<Tag> {
  @Query("SELECT t FROM Tag t WHERE t.tag_title =?1")
  Optional<Tag> findByTagTitle(String title);

  @Query("SELECT b.tags FROM Bookmark b WHERE b.id =?1")
  List<Tag> findTagsByBookmarkId(long Id);

  @Query("SELECT t FROM Tag t WHERE t.tag_title IN ?1")
  List<Tag> findByTagTitles(List<String> tagTitles);
}
