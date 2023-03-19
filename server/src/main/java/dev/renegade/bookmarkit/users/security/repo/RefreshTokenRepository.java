package dev.renegade.bookmarkit.users.security.repo;

import dev.renegade.bookmarkit.users.model.User;
import dev.renegade.bookmarkit.users.security.model.refreshToken.RefreshToken;
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
