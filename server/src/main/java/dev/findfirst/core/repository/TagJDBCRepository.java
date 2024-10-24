package dev.findfirst.core.repository;

import dev.findfirst.core.model.TagJDBC;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface TagJDBCRepository extends CrudRepository<TagJDBC, Long> {
  Optional<TagJDBC> findByTagTitle(String title);
}
