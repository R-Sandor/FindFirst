package dev.findfirst.core.users.service;

import dev.findfirst.core.model.VerificationToken;
import dev.findfirst.core.users.model.user.User;
import dev.findfirst.core.users.repository.UserRepo;
import dev.findfirst.core.users.repository.VerificationTokenRepository;
import dev.findfirst.security.execeptions.NoUserFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepo userRepo;
  private final VerificationTokenRepository tokenRepository;

  public User getUserByEmail(String email) {
    return userRepo.findByEmail(email).orElseThrow(NoUserFoundException::new);
  }

  public User getUserByUsername(String username) {
    return userRepo.findByUsername(username).orElseThrow(NoUserFoundException::new);
  }

  public User saveUser(User user) {
    return userRepo.save(user);
  }

  public VerificationToken getVerificationToken(String VerificationToken) {
    return tokenRepository.findByToken(VerificationToken);
  }
}
