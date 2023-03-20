package dev.renegade.bookmarkit.security.repo;

import dev.renegade.bookmarkit.security.model.refreshToken.RefreshToken;
import dev.renegade.bookmarkit.users.model.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);
}
