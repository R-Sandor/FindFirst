package dev.findfirst.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.findfirst.users.model.user.Token;
import dev.findfirst.users.model.user.User;

public interface PasswordTokenRepository extends JpaRepository<Token, Long> {

  public Token findByToken(String token);

  public Token findByUser(User user);
}
