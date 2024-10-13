package dev.findfirst.security.jwt.repo;

import java.util.Optional;

import dev.findfirst.security.userAuth.models.RefreshToken;
import dev.findfirst.users.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);
}
