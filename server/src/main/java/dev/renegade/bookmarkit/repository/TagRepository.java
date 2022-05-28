package dev.renegade.bookmarkit.repository;

import dev.renegade.bookmarkit.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TagRepository extends JpaRepository<Tag, Long> {
  @Query("SELECT t FROM Tag t WHERE t.title =?1")
  Tag findByTitle(String title);
}
