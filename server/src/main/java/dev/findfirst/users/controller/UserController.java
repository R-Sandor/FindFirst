package dev.findfirst.users.controller;

import dev.findfirst.security.userAuth.execeptions.NoTokenFoundException;
import dev.findfirst.security.userAuth.execeptions.NoUserFoundException;
import dev.findfirst.security.userAuth.execeptions.TokenExpiredException;
import dev.findfirst.security.userAuth.models.payload.request.SignupRequest;
import dev.findfirst.security.userAuth.models.payload.response.MessageResponse;
import dev.findfirst.users.exceptions.EmailAlreadyRegisteredException;
import dev.findfirst.users.exceptions.UserNameTakenException;
import dev.findfirst.users.model.user.TokenPassword;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.service.ForgotPasswordService;
import dev.findfirst.users.service.RegistrationService;
import dev.findfirst.users.service.UserManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.UnexpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserManagementService userService;

  private final RegistrationService regService;

  private final ForgotPasswordService pwdService;

  @Value("${bookmarkit.app.frontend-url:http://localhost:3000/}") private String frontendUrl;

  @GetMapping("api/user/regitrationConfirm")
  public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token)
      throws URISyntaxException {

    HttpHeaders httpHeaders = new HttpHeaders();
    URI findfirst = new URI(frontendUrl);
    try {
      regService.registrationComplete(token);
    } catch (NoTokenFoundException | TokenExpiredException | NoUserFoundException e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
    httpHeaders.setLocation(findfirst);
    return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
  }

  @PostMapping("api/user/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    User user;
    try {
      user = userService.createNewUserAccount(signUpRequest);
    } catch (UserNameTakenException | EmailAlreadyRegisteredException | UnexpectedException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    regService.sendRegistration(user);
    return ResponseEntity.ok(new MessageResponse("User Account Created, Complete Registration!"));
  }

  @PostMapping("api/user/resetPassword")
  public ResponseEntity<String> resetPassword(@RequestParam @Email String email) {
    try {
      pwdService.sendResetToken(email);
      return ResponseEntity.ok().body("Password Reset sent");
    } catch (NoUserFoundException e) {
      return ResponseEntity.badRequest().body("User does not exist");
    }
  }

  @GetMapping("api/user/changePassword")
  public ResponseEntity<String> frontendPasswordWithToken(@RequestParam("token") String token)
      throws URISyntaxException {

    HttpHeaders httpHeaders = new HttpHeaders();
    URI findfirst = new URI(frontendUrl + "/account/resetPassword/" + token);
    try {
      pwdService.sendResetToken(token);
    } catch (NoUserFoundException e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
    httpHeaders.setLocation(findfirst);
    return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
  }

  @PostMapping("api/user/changePassword")
  public ResponseEntity<String> passwordChange(
      @RequestParam("tokenPassword") TokenPassword tokenPassword) throws URISyntaxException {
    try {
      pwdService.changePassword(tokenPassword);
    } catch (NoTokenFoundException | TokenExpiredException | NoUserFoundException e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("Password changed", HttpStatus.SEE_OTHER);
  }
}
