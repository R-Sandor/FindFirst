package dev.findfirst.users.service;

import java.util.Calendar;

import jakarta.validation.constraints.Email;

import dev.findfirst.users.exceptions.NoTokenFoundException;
import dev.findfirst.users.exceptions.NoUserFoundException;
import dev.findfirst.users.exceptions.TokenExpiredException;
import dev.findfirst.users.model.user.Token;
import dev.findfirst.users.model.user.TokenPassword;
import dev.findfirst.users.model.user.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForgotPasswordService extends AccountService {

  public ForgotPasswordService(UserManagementService userManagement, DefaultEmailService email) {
    super(userManagement, email);
  }

  public void sendResetToken(@Email String email) throws NoUserFoundException {
    log.debug("sending password reset for {}", email);
    User user = userManagement.getUserByEmail(email);
    if (user == null) {
      throw new NoUserFoundException();
    }
    var token = userManagement.createResetPwdToken(user);
    accountEmailOp(email, token);
  }

  public void changePassword(TokenPassword tp)
      throws NoTokenFoundException, TokenExpiredException, NoUserFoundException {
    validatePasswordResetToken(tp.token());
    User user = userManagement.getUserFromPasswordToken(tp.token());
    userManagement.changeUserPassword(user, tp.password());
  }

  @Override
  void accountEmailOp(String emailAddress, String token) {
    String confirmationUrl = this.domain + "/user/changePassword?token=" + token;
    String message = """
            You have requested password reset for your account.
            Please complete the password reset with the given url:
            %s

            If this request not made by you, ignore this message and consider
            changing your password.

            Sincerly,
            Findfirst team!
        """.formatted(confirmationUrl);

    emailService.sendSimpleEmail(emailAddress, "Account Registration", message);
  }

  public boolean validatePasswordResetToken(String token)
      throws NoTokenFoundException, TokenExpiredException {
    final Token passToken = userManagement.getPasswordToken(token);
    if (!isTokenFound(passToken))
      throw new NoTokenFoundException();
    if (isTokenExpired(passToken)) {
      throw new TokenExpiredException();
    }
    return true;
  }

  private boolean isTokenFound(Token token) {
    return token != null && token.getUser() != null;
  }

  private boolean isTokenExpired(Token passToken) {
    final Calendar cal = Calendar.getInstance();
    return passToken.getExpiryDate().before(cal.getTime());
  }
}
