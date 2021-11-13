package dev.renegade.bookmarkit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
