package dev.findfirst.users.repository;

import java.util.Optional;

import dev.findfirst.users.model.user.Role;
import dev.findfirst.users.model.user.URole;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
  Optional<Role> findByName(URole name);

  Optional<Role> findById(int id);
}
