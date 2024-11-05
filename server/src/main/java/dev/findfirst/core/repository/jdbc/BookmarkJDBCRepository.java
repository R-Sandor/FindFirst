package dev.findfirst.core.repository.jdbc;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;

import org.springframework.data.repository.CrudRepository;

public interface BookmarkJDBCRepository extends CrudRepository<BookmarkJDBC, Long> {
}
