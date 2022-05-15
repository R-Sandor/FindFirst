package dev.renegade.bookmarkit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import dev.renegade.bookmarkit.model.Tag;


public interface TagRepository extends JpaRepository<Tag, Long>{
}
