package dev.findfirst.users.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.findfirst.users.model.user.User;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
}
