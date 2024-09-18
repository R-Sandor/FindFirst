package dev.findfirst.users.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.UnexpectedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import dev.findfirst.security.jwt.exceptions.TokenRefreshException;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.userAuth.models.RefreshToken;
import dev.findfirst.security.userAuth.models.TokenRefreshResponse;
import dev.findfirst.security.userAuth.models.payload.request.SignupRequest;
import dev.findfirst.security.userAuth.models.payload.request.TokenRefreshRequest;
import dev.findfirst.security.userAuth.models.payload.response.MessageResponse;
import dev.findfirst.users.exceptions.EmailAlreadyRegisteredException;
import dev.findfirst.users.exceptions.NoTokenFoundException;
import dev.findfirst.users.exceptions.NoUserFoundException;
import dev.findfirst.users.exceptions.TokenExpiredException;
import dev.findfirst.users.exceptions.UserNameTakenException;
import dev.findfirst.users.model.user.SigninTokens;
import dev.findfirst.users.model.user.TokenPassword;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.service.ForgotPasswordService;
import dev.findfirst.users.service.RegistrationService;
import dev.findfirst.users.service.UserManagementService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserManagementService userService;

  private final RegistrationService regService;

  private final ForgotPasswordService pwdService;

  private final RefreshTokenService refreshTokenService;

  @Value("${findfirst.app.frontend-url}") private String frontendUrl;

  @Value("${findfirst.app.domain}") private String domain;

  @PostMapping("/signup")
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

  @GetMapping("/regitrationConfirm")
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

  @PostMapping("/resetPassword")
  public ResponseEntity<String> resetPassword(@RequestParam @Email String email) {
    try {
      log.debug("resetting password");
      pwdService.sendResetToken(email);
      return ResponseEntity.ok().body("Password Reset sent");
    } catch (NoUserFoundException e) {
      return ResponseEntity.badRequest().body("User does not exist");
    }
  }

  @GetMapping("changePassword")
  public ResponseEntity<String> frontendPasswordWithToken(@RequestParam("token") String token)
      throws URISyntaxException {

    HttpHeaders httpHeaders = new HttpHeaders();
    URI findfirst = new URI(frontendUrl + "/account/resetPassword/" + token);
    try {
      pwdService.validatePasswordResetToken(token);
    } catch (NoTokenFoundException | TokenExpiredException e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
    httpHeaders.setLocation(findfirst);
    return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
  }

  @PostMapping("changePassword")
  public ResponseEntity<String> passwordChange(@RequestBody TokenPassword tokenPassword)
      throws URISyntaxException {
    try {
      pwdService.changePassword(tokenPassword);
    } catch (NoTokenFoundException | TokenExpiredException | NoUserFoundException e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("Password changed", HttpStatus.SEE_OTHER);
  }

  @PostMapping("/signin")
  public ResponseEntity<?> token(@RequestHeader(value = "Authorization") String authorization) {
    SigninTokens tkns;
    try {
      tkns = userService.signinUser(authorization);
    } catch (NoUserFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    ResponseCookie cookie = ResponseCookie.from("findfirst", tkns.jwt()).secure(true) // enable
                                                                                      // this when
                                                                                      // we are
                                                                                      // using
                                                                                      // https
        // .sameSite("strict")
        .path("/").domain(domain).httpOnly(true).build();

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new TokenRefreshResponse(tkns.refreshToken()));
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<?> refreshToken(@RequestParam("token") TokenRefreshRequest refreshRequest) {
    String jwt = refreshRequest.refreshToken();
    return (ResponseEntity<?>) refreshTokenService.findByToken(jwt)
        .map(refreshTokenService::verifyExpiration).map(RefreshToken::getUser).map(user -> {
          String token;
          token = userService.generateTokenFromUser(user);
          ResponseCookie cookie = ResponseCookie.from("findfirst", token).secure(true)
              .sameSite("strict").path("/").domain(domain).httpOnly(true).build();
          return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(token);
        }).orElseThrow(() -> new TokenRefreshException(jwt, "Refresh token is not in database!"));
  }
}
