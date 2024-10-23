package dev.findfirst.core.repository;

import dev.findfirst.core.model.BookmarkJDBC;

import org.springframework.data.repository.CrudRepository;

public interface BookmarkJDBCRepository extends CrudRepository<BookmarkJDBC, Long> {
}
