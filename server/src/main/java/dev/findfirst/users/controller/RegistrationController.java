package dev.findfirst.users.controller;

import dev.findfirst.users.model.user.User;
import dev.findfirst.users.model.user.VerificationToken;
import dev.findfirst.users.service.UserService;
import java.util.Calendar;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
  private final UserService service;

  @GetMapping("/regitrationConfirm")
  public ResponseEntity<String> confirmRegistration(
      WebRequest request, Model model, @RequestParam("token") String token) {
    String message;
    VerificationToken verificationToken = service.getVerificationToken(token);
    if (verificationToken == null) {
      message = "auth.message.invalidToken";
    }

    User user = verificationToken.getUser();
    Calendar cal = Calendar.getInstance();
    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
      message = "auth.message.expired";
    }
    message = "Successful registration";
    user.setEnabled(true);
    service.saveUser(user);
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
}
