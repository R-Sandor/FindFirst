package dev.findfirst.core.users.repository;

import dev.findfirst.core.model.VerificationToken;
import dev.findfirst.core.users.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  public VerificationToken findByToken(String token);

  public VerificationToken findByUser(User user);
}
