package dev.findfirst.core.repository;

import org.springframework.data.repository.CrudRepository;

import dev.findfirst.core.model.BookmarkTag;
import java.util.List;

public interface BookmarkTagRepository extends CrudRepository<BookmarkTag, Long> {
    List<BookmarkTag> findByBookmarkId(Long bookmarkId);

    List<BookmarkTag> findByTagId(Long tagId);
}