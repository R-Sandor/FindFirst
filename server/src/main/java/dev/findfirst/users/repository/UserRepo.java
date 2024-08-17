package dev.findfirst.users.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.findfirst.users.model.user.User;

public interface UserRepo extends CrudRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
