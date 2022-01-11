package dev.renegade.bookmarkit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import dev.renegade.bookmarkit.model.Bookmark;

@RepositoryRestResource
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
