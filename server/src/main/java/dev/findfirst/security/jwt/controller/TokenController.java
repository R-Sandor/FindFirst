package dev.findfirst.security.jwt.controller;

import dev.findfirst.security.jwt.JwtService;
import dev.findfirst.security.jwt.exceptions.TokenRefreshException;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.userAuth.models.RefreshToken;
import dev.findfirst.security.userAuth.models.TokenRefreshResponse;
import dev.findfirst.security.userAuth.models.payload.request.TokenRefreshRequest;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.service.UserService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

  @Value("${bookmarkit.app.domain:localhost}") private String domain;

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
}
