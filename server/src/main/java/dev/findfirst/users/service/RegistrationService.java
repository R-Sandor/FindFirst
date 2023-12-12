package dev.findfirst.users.service;

import dev.findfirst.security.userAuth.execeptions.NoUserFoundException;
import dev.findfirst.security.userAuth.execeptions.NoTokenFoundException;
import dev.findfirst.security.userAuth.execeptions.TokenExpiredException;
import dev.findfirst.users.model.user.Token;
import dev.findfirst.users.model.user.User;
import java.util.Calendar;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final UserManagementService userService;

  private final DefaultEmailService mail;

  @Value("${bookmarkit.app.domain:http://localhost:9000/api}") private String domain;

  public void sendRegistration(User user) {
    String token = UUID.randomUUID().toString();
    userService.createVerificationToken(user, token);

    String confirmationUrl = domain + "/regitrationConfirm?token=" + token;

    String message =
        """
            Please finish registring your account with the given url:
            %s

            Sincerly,
            Findfirst team!
        """
            .formatted(confirmationUrl);

    mail.sendSimpleEmail(user.getEmail(), "Account Registration", message);
  }

  public void registrationComplete(String token)
      throws NoTokenFoundException, TokenExpiredException, NoUserFoundException {
    Token verificationToken = userService.getVerificationToken(token);
    if (verificationToken == null) {
      throw new NoTokenFoundException();
    }

    User user = verificationToken.getUser();
    if (user == null) {
      throw new NoUserFoundException();
    }
    Calendar cal = Calendar.getInstance();
    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
      throw new TokenExpiredException();
    }
    user.setEnabled(true);
    userService.saveUser(user);
  }
}
