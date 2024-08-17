package dev.findfirst.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.Tag;
import dev.findfirst.security.userAuth.tenant.repository.TenantableRepository;

@RepositoryRestResource
public interface BookmarkRepository extends TenantableRepository<Bookmark> {
  @Query("SELECT distinct b from Bookmark b inner join b.tags bt where bt = ?1")
  List<Bookmark> findByTag(Tag tag);

  Optional<Bookmark> findByUrl(String url);
}
