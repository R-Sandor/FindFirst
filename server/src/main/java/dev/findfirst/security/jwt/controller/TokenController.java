package dev.findfirst.security.jwt.controller;

import dev.findfirst.security.jwt.exceptions.TokenRefreshException;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.userAuth.models.RefreshToken;
import dev.findfirst.security.userAuth.models.payload.request.TokenRefreshRequest;
import dev.findfirst.users.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TokenController {

  @Autowired JwtEncoder encoder;
  @Autowired RefreshTokenService refreshTokenService;
  @Autowired AuthenticationManager authenticationManager;

  @Value("${findfirst.app.jwtExpirationMs}") private int jwtExpirationMs;

  @Value("${findfirst.app.domain:localhost}") private String domain;

  @Autowired UserManagementService userService;

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
                  String token;
                  token = userService.generateTokenFromUser(user);
                  ResponseCookie cookie =
                      ResponseCookie.from("findfirst", token)
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
}
