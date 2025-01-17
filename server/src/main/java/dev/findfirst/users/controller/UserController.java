package dev.findfirst.users.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java.util.Optional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import dev.findfirst.security.jwt.exceptions.TokenRefreshException;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.userauth.context.UserContext;
import dev.findfirst.security.userauth.models.RefreshToken;
import dev.findfirst.security.userauth.models.TokenRefreshResponse;
import dev.findfirst.security.userauth.models.payload.request.SignupRequest;
import dev.findfirst.security.userauth.models.payload.request.TokenRefreshRequest;
import dev.findfirst.security.userauth.models.payload.response.MessageResponse;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserManagementService userService;

  private final UserContext uContext;

  private final RegistrationService regService;

  private final ForgotPasswordService pwdService;

  private final RefreshTokenService refreshTokenService;

  @Value("${findfirst.app.frontend-url}")
  private String frontendUrl;

  @Value("${findfirst.app.domain}")
  private String domain;

  @Value("${findfirst.upload.max-file-size}")
  private int maxFileSize;

  @Value("${findfirst.upload.allowed-types}")
  private String[] allowedTypes;

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    User user;
    try {
      user = userService.createNewUserAccount(signUpRequest);
    } catch (UserNameTakenException | EmailAlreadyRegisteredException | UnexpectedException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    regService.sendRegistration(user);
    return ResponseEntity
        .ok(new MessageResponse("User Account Created, Complete Registration!").toString());
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
  public ResponseEntity<String> passwordChange(@RequestBody TokenPassword tokenPassword) {
    try {
      pwdService.changePassword(tokenPassword);
    } catch (NoTokenFoundException | TokenExpiredException | NoUserFoundException e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>("Password changed", HttpStatus.SEE_OTHER);
  }

  @PostMapping("/signin")
  public ResponseEntity<TokenRefreshResponse> token(
      @RequestHeader(value = "Authorization") String authorization) {
    SigninTokens tkns;
    try {
      tkns = userService.signinUser(authorization);
    } catch (NoUserFoundException e) {
      return ResponseEntity.badRequest().body(new TokenRefreshResponse(null, null, e.toString()));
    }

    ResponseCookie cookie = ResponseCookie.from("findfirst", tkns.jwt()).secure(true).path("/")
        .domain(domain).httpOnly(true).build();

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new TokenRefreshResponse(tkns.refreshToken()));
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<String> refreshToken(
      @RequestParam("token") TokenRefreshRequest refreshRequest) {
    String jwt = refreshRequest.refreshToken();
    return refreshTokenService.findByToken(jwt).map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser).map(user -> {
          String token = userService.generateTokenFromUser(user.getId());
          ResponseCookie cookie = ResponseCookie.from("findfirst", token).secure(true)
              .sameSite("strict").path("/").domain(domain).httpOnly(true).build();
          return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(token);
        }).orElseThrow(() -> new TokenRefreshException(jwt, "Refresh token is not in database!"));
  }

  @PostMapping("/profile-picture")
  public ResponseEntity<?> uploadProfilePicture(@RequestParam("file") @Size(max = maxFileSize) MultipartFile file) {

    // File type validation
    String contentType = file.getContentType();
    if (Arrays.stream(allowedTypes).noneMatch(contentType::equals)) {
      return ResponseEntity.badRequest().body("Invalid file type. Only JPG and PNG are allowed.");
    }

    try {
      User user = userService.getUserById(uContext.getUserId()).orElseThrow(NoUserFoundException::new);
      userService.changeUserPhoto(user, file);

      return ResponseEntity.ok("File uploaded successfully.");
    } catch (NoUserFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
    }
  }

  @GetMapping("/profile-picture")
  public ResponseEntity<Resource> getUserProfilePicture(@RequestParam("userId") int userId) {
    try {
      User user;
      try {
        user = userService.getUserById(userId).orElseThrow(() -> new NoUserFoundException());
      } catch (NoUserFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      String userPhotoPath = user.getUserPhoto();

      if (userPhotoPath == null || userPhotoPath.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      // Load file
      File photoFile = new File(userPhotoPath);
      if (!photoFile.exists()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

      // Create response
      Resource fileResource = new FileSystemResource(photoFile);
      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(Files.probeContentType(photoFile.toPath())))
              .body(fileResource);

    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
