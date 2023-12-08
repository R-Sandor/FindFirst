package dev.findfirst.bookmarkit.users.repository;

import dev.findfirst.bookmarkit.model.VerificationToken;
import dev.findfirst.bookmarkit.users.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  public VerificationToken findByToken(String token);

  public VerificationToken findByUser(User user);
}
