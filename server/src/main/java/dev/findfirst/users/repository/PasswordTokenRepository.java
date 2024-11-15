package dev.findfirst.users.repository;

import dev.findfirst.users.model.user.Token;
import dev.findfirst.users.model.user.User;

import org.springframework.data.repository.CrudRepository;

public interface PasswordTokenRepository extends CrudRepository<Token, Long> {

  public Token findByToken(String token);

  public Token findByUser(User user);
}
