package dev.renegade.bookmarkit.users.repo;

import dev.renegade.bookmarkit.users.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

  Optional<User> findByEmail(String email);
}
