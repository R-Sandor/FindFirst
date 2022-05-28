package dev.renegade.bookmarkit.repository;

import dev.renegade.bookmarkit.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {}
