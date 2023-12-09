package dev.findfirst.security.controller;

import dev.findfirst.users.model.user.URole;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.RoleRepository;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.service.UserService;
import dev.findfirst.security.execeptions.TokenRefreshException;
import dev.findfirst.security.jwt.JwtService;
import dev.findfirst.security.model.payload.TokenRefreshResponse;
import dev.findfirst.security.model.payload.request.SignupRequest;
import dev.findfirst.security.model.payload.request.TokenRefreshRequest;
import dev.findfirst.security.model.payload.response.MessageResponse;
import dev.findfirst.security.model.refreshToken.RefreshToken;
import dev.findfirst.security.service.RefreshTokenService;
import dev.findfirst.security.tenant.data.TenantService;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.NoSuchElementException;
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
  @Autowired private JwtService jwtService;
  @Autowired RefreshTokenService refreshTokenService;
  @Autowired AuthenticationManager authenticationManager;

  @Value("${bookmarkit.app.jwtExpirationMs}") private int jwtExpirationMs;

  @Value("${bookmarkit.app.domain}") private String domain;

  @Autowired UserRepo userRepository;

  @Autowired TenantService tenantService;

  @Autowired RoleRepository roleRepository;

  @Autowired PasswordEncoder passwdEncoder;

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
    final RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
    String token = jwtService.generateTokenFromUser(user);

    ResponseCookie cookie =
        ResponseCookie.from("bookmarkit", token)
            .secure(false) // enable this when we are using https
            // .sameSite("strict")
            .path("/")
            .domain(domain)
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
                  String token = jwtService.generateTokenFromUsername(user.getUsername());

                  ResponseCookie cookie =
                      ResponseCookie.from("bookmarkit", token)
                          .secure(false)
                          // .sameSite("strict")
                          .path("/")
                          .domain(domain)
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
    User user = new User(signUpRequest, passwdEncoder.encode(signUpRequest.password()));
    var role =
        roleRepository.findById(URole.ROLE_USER.ordinal()).orElseThrow(NoSuchElementException::new);
    user.setRole(role);
    var t = tenantService.create(signUpRequest.username());
    // create a new tenant
    try {
      user.setTenantId(t.getId());
      userRepository.save(user);
    } catch (Exception e) {
      // If any exception occurs we should delete the records that were just made.
      tenantService.deleteById(t.getId());
      userRepository.delete(user);
      return ResponseEntity.badRequest().body(new MessageResponse("Could not signup, try again."));
    }
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
