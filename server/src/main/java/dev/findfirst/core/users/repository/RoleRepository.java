package dev.findfirst.core.users.repository;

import dev.findfirst.core.users.model.user.Role;
import dev.findfirst.core.users.model.user.URole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByName(URole name);

  Optional<Role> findById(Integer id);
}
