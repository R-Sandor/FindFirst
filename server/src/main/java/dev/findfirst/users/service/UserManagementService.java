package dev.findfirst.users.service;

import dev.findfirst.security.userAuth.execeptions.NoUserFoundException;
import dev.findfirst.users.model.user.Token;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.PasswordTokenRepository;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManagementService {

  private final UserRepo userRepo;
  private final VerificationTokenRepository tokenRepository;
  private final PasswordTokenRepository passwordTokenRepository;

  public User getUserByEmail(String email) throws NoUserFoundException {
    return userRepo.findByEmail(email).orElseThrow(NoUserFoundException::new);
  }

  public User getUserByUsername(String username) throws NoUserFoundException {
    return userRepo.findByUsername(username).orElseThrow(NoUserFoundException::new);
  }

  public boolean getUserExistByUsername(String username) {
    return userRepo.existsByUsername(username);
  }

  public boolean getUserExistEmail(String email) {
    return userRepo.existsByEmail(email);
  }

  public User saveUser(User user) {
    return userRepo.save(user);
  }

  public void deleteUser(User user) {
    userRepo.delete(user);
  }
  
  public void changeUserPassword(User user, String password) {
    user.setPassword(password);
    saveUser(user);
  }

  public void createVerificationToken(User user, String token) {
    Token verificationToken = new Token(user, token);
    tokenRepository.save(verificationToken);
  }

  public Token getVerificationToken(String VerificationToken) {
    return tokenRepository.findByToken(VerificationToken);
  }

  public void createResetPwdToken(User user, String token) {
    Token pwdToken = new Token(user, token);
    passwordTokenRepository.save(pwdToken);
  }

  public Token getPasswordToken(String pwdToken) {
    return passwordTokenRepository.findByToken(pwdToken);
  }

  public User getUserFromPasswordToken(String pwdToken) {
    return passwordTokenRepository.findByToken(pwdToken).getUser();
  }

}
