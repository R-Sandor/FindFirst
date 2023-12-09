package dev.findfirst.users.repository;

import dev.findfirst.users.model.user.User;
import dev.findfirst.users.model.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  public VerificationToken findByToken(String token);

  public VerificationToken findByUser(User user);
}
