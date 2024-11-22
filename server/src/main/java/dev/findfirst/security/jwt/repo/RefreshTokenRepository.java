package dev.findfirst.security.jwt.repo;

import java.util.Optional;

import dev.findfirst.security.userauth.models.RefreshToken;
import dev.findfirst.users.model.user.User;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  int deleteByUser(User user);
}
