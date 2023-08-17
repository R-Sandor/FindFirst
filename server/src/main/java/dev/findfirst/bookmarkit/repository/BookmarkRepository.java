package dev.findfirst.bookmarkit.repository;

import dev.findfirst.bookmarkit.model.Bookmark;
import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.model.TagCntRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
  @Query("SELECT distinct b from Bookmark b inner join b.tags bt where bt = ?1")
  List<Bookmark> findByTag(Tag tag);


}
