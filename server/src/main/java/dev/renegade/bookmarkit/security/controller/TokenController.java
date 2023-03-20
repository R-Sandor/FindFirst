package dev.renegade.bookmarkit.security.controller;

import dev.renegade.bookmarkit.security.execeptions.TokenRefreshException;
import dev.renegade.bookmarkit.security.jwt.JwtUtils;
import dev.renegade.bookmarkit.security.model.refreshToken.RefreshToken;
import dev.renegade.bookmarkit.security.model.refreshToken.TokenRefreshRequest;
import dev.renegade.bookmarkit.security.model.refreshToken.TokenRefreshResponse;
import dev.renegade.bookmarkit.security.service.RefreshTokenService;
import dev.renegade.bookmarkit.users.model.User;
import dev.renegade.bookmarkit.users.service.UserService;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TokenController {
  @Autowired JwtEncoder encoder;
  @Autowired private JwtUtils jwtUtils;
  @Autowired RefreshTokenService refreshTokenService;
  @Autowired private UserService userService;
  @Value("${bookmarkit.app.jwtExpirationMs}") private int jwtExpirationMs;

  @PostMapping("/auth/signin")
  public ResponseEntity<?> token(Authentication authentication) {
    Instant now = Instant.now();
    // @formatter:off
    String scope =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

    // Optional<User> user = userService.getUserByEmail(authentication.getName());
    // if(user.isPresent()) {
    // RefreshToken refreshToken = refreshTokenService.createRefreshToken((user.get().getId()));
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusMillis(jwtExpirationMs))
            .subject(authentication.getName())
            .claim("scope", scope)
            .build();
    ResponseCookie cookie =
        ResponseCookie.from(
                "bookmarkit",
                this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue())
            .secure(false)
            .path("/")
            .domain("localhost")
            .httpOnly(true)
            .build();
              return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, cookie.toString())
              .body("TEST");
    // }
    // return ResponseEntity.badRequest().body("User does not exist.");
    
  }

  @PostMapping("/auth/refreshToken")
  public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest refreshRequest) {
    String jwt = refreshRequest.refreshToken();

    return (ResponseEntity<?>) refreshTokenService
        .findByToken(jwt)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(
            user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok()
              .header(HttpHeaders.SET_COOKIE, token)
              .body(token);
            })
        .orElseThrow(() -> new TokenRefreshException(jwt, "Refresh token is not in database!"));
  }
}
