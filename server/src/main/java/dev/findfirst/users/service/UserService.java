package dev.findfirst.users.service;

import dev.findfirst.security.userAuth.execeptions.NoUserFoundException;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.model.user.VerificationToken;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.repository.VerificationTokenRepository;
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
