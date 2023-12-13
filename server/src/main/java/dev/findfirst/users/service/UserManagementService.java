package dev.findfirst.users.service;

import dev.findfirst.security.userAuth.execeptions.NoUserFoundException;
import dev.findfirst.security.userAuth.models.payload.request.SignupRequest;
import dev.findfirst.security.userAuth.tenant.data.TenantService;
import dev.findfirst.users.exceptions.EmailAlreadyRegisteredException;
import dev.findfirst.users.exceptions.UserNameTakenException;
import dev.findfirst.users.model.user.Token;
import dev.findfirst.users.model.user.URole;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.PasswordTokenRepository;
import dev.findfirst.users.repository.RoleRepository;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.repository.VerificationTokenRepository;
import java.rmi.UnexpectedException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManagementService {

  private final UserRepo userRepo;
  private final VerificationTokenRepository tokenRepository;
  private final PasswordTokenRepository passwordTokenRepository;
  private final PasswordEncoder passwdEncoder;
  private final RoleRepository roleRepository;
  private final TenantService tenantService;

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

  public User createNewUserAccount(SignupRequest signupRequest)
      throws UserNameTakenException, EmailAlreadyRegisteredException, UnexpectedException {
    if (getUserExistByUsername(signupRequest.username())) {
      throw new UserNameTakenException();
    }

    if (getUserExistEmail(signupRequest.email())) {
      throw new EmailAlreadyRegisteredException();
    }

    // Create new user's account
    User user = new User(signupRequest, passwdEncoder.encode(signupRequest.password()));
    var role =
        roleRepository.findById(URole.ROLE_USER.ordinal()).orElseThrow(NoSuchElementException::new);
    user.setRole(role);
    var t = tenantService.create(signupRequest.username());

    // create a new tenant
    try {
      user.setTenantId(t.getId());
      return saveUser(user);
    } catch (Exception e) {
      // If any exception occurs we should delete the records that were just made.
      tenantService.deleteById(t.getId());
      deleteUser(user);
      throw new UnexpectedException("Unexpected error occured during signup, try again");
    }
  }
}
