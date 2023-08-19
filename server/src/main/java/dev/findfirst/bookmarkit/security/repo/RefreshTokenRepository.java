package dev.findfirst.bookmarkit.security.repo;

import dev.findfirst.bookmarkit.security.model.refreshToken.RefreshToken;
import dev.findfirst.bookmarkit.users.model.user.User;
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
