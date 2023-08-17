package dev.findfirst.bookmarkit.repository;

import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.model.TagCntRecord;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepoCustom {
  @Query("SELECT t FROM Tag t WHERE t.title =?1")
  Tag findByTitle(String title);


  //   @Query(
  //   "SELECT new dev.findfirst.bookmarkit.model.TagCntRecord(" +
  //   "new dev.findfirst.bookmarkit.model.Tag(tag.id, tag.title, tag.url), COUNT(tag.id))" +
  //   "FROM tag JOIN bookmark ON bookmark.tag_id" +
  //   "GROUP BY tag.id"
  // )
  // List<TagCntRecord> test();
}
