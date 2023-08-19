package dev.findfirst.bookmarkit.security.controller;

import dev.findfirst.bookmarkit.security.execeptions.TokenRefreshException;
import dev.findfirst.bookmarkit.security.jwt.JwtUtils;
import dev.findfirst.bookmarkit.security.model.payload.TokenRefreshResponse;
import dev.findfirst.bookmarkit.security.model.payload.request.SignupRequest;
import dev.findfirst.bookmarkit.security.model.payload.request.TokenRefreshRequest;
import dev.findfirst.bookmarkit.security.model.payload.response.MessageResponse;
import dev.findfirst.bookmarkit.security.model.refreshToken.RefreshToken;
import dev.findfirst.bookmarkit.security.service.RefreshTokenService;
import dev.findfirst.bookmarkit.users.model.user.URole;
import dev.findfirst.bookmarkit.users.model.user.User;
import dev.findfirst.bookmarkit.users.repository.RoleRepository;
import dev.findfirst.bookmarkit.users.repository.UserRepo;
import dev.findfirst.bookmarkit.users.service.UserService;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TokenController {

  @Autowired JwtEncoder encoder;
  @Autowired private JwtUtils jwtUtils;
  @Autowired RefreshTokenService refreshTokenService;
  @Autowired AuthenticationManager authenticationManager;

  @Value("${bookmarkit.app.jwtExpirationMs}") private int jwtExpirationMs;

  @Autowired UserRepo userRepository;

  @Autowired RoleRepository roleRepository;

  @Autowired PasswordEncoder pEncoder;

  @Autowired UserService userService;

  @PostMapping("/auth/signin")
  public ResponseEntity<?> token(@RequestHeader(value = "Authorization") String authorization) {
    String base64Credentials = authorization.substring("Basic".length()).trim();
    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
    // credentials = username:password
    final String[] values = credentials.split(":", 2);

    // This error should never occur, as authentication checks username and throws.
    User user = userService.getUserByUsername(values[0]);
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
    String token = jwtUtils.generateTokenFromUser(user);

    ResponseCookie cookie =
        ResponseCookie.from("bookmarkit", token)
            .secure(false)
            .path("/")
            .domain("localhost")
            .httpOnly(true)
            .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new TokenRefreshResponse(refreshToken.getToken()));
  }

  @PostMapping("/auth/refreshToken")
  public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest refreshRequest) {
    String jwt = refreshRequest.refreshToken();

    return (ResponseEntity<?>)
        refreshTokenService
            .findByToken(jwt)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(
                user -> {
                  String token = jwtUtils.generateTokenFromUsername(user.getUsername());

                  ResponseCookie cookie =
                      ResponseCookie.from("bookmarkit", token)
                          .secure(false)
                          .path("/")
                          .domain("localhost")
                          .httpOnly(true)
                          .build();
                  return ResponseEntity.ok()
                      .header(HttpHeaders.SET_COOKIE, cookie.toString())
                      .body(token);
                })
            .orElseThrow(() -> new TokenRefreshException(jwt, "Refresh token is not in database!"));
  }

  @PostMapping("/auth/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.username())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.email())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user =
        new User(
            signUpRequest.username(),
            signUpRequest.email(),
            pEncoder.encode(signUpRequest.password()));

    user.setRole(roleRepository.findByName(URole.ROLE_USER).get());
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
