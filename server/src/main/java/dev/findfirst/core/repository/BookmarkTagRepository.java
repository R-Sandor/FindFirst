package dev.findfirst.core.repository;

import java.util.List;

import dev.findfirst.core.model.BookmarkTag;

import org.springframework.data.repository.CrudRepository;

public interface BookmarkTagRepository extends CrudRepository<BookmarkTag, Long> {
  List<BookmarkTag> findByBookmarkId(Long bookmarkId);

  List<BookmarkTag> findByTagId(Long tagId);
}
