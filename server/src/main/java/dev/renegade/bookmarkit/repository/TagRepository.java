package dev.renegade.bookmarkit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import dev.renegade.bookmarkit.model.Tag;

@RepositoryRestResource
public interface TagRepository extends JpaRepository<Tag, Long>{

    Tag findByTitle(String title);
}
