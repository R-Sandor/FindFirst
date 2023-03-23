package dev.renegade.bookmarkit.security.controller;

import dev.renegade.bookmarkit.security.execeptions.TokenRefreshException;
import dev.renegade.bookmarkit.security.jwt.JwtUtils;
import dev.renegade.bookmarkit.security.model.payload.TokenRefreshResponse;
import dev.renegade.bookmarkit.security.model.payload.request.SignupRequest;
import dev.renegade.bookmarkit.security.model.payload.request.TokenRefreshRequest;
import dev.renegade.bookmarkit.security.model.payload.response.MessageResponse;
import dev.renegade.bookmarkit.security.model.refreshToken.RefreshToken;
import dev.renegade.bookmarkit.security.service.RefreshTokenService;
import dev.renegade.bookmarkit.users.model.ERole;
import dev.renegade.bookmarkit.users.model.Role;
import dev.renegade.bookmarkit.users.model.User;
import dev.renegade.bookmarkit.users.repository.RoleRepository;
import dev.renegade.bookmarkit.users.repository.UserRepo;
import dev.renegade.bookmarkit.users.service.UserService;
import jakarta.validation.Valid;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
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
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(values[0], values[1]));
    Instant now = Instant.now();
    // @formatter:off
    String scope =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

    // This error should never occur, as authentication checks username and throws. 
    User user = userService.getUserByUsername(values[0]).orElseThrow(() -> new RuntimeException("No such user"));
    RefreshToken refreshToken = refreshTokenService.createRefreshToken((user.getId()));
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
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new TokenRefreshResponse(refreshToken.getToken()));
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
                  ResponseCookie.from(
                    "bookmarkit", token)
                    .secure(false)
                    .path("/")
                    .domain("localhost")
                    .httpOnly(true)
                    .build();
                  return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(token);
                })
            .orElseThrow(() -> new TokenRefreshException(jwt, "Refresh token is not in database!"));
  }

  @PostMapping("/auth/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.username())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.email())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.username(), signUpRequest.email(),
        pEncoder.encode(signUpRequest.password()));

    // Set<String> strRoles = signUpRequest.role();
    // Set<Role> roles = new HashSet<>();

    // if (strRoles == null) {
    //   Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    //       .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //   roles.add(userRole);
    // } else {
    //   strRoles.forEach(role -> {
    //     switch (role) {
    //     case "admin":
    //       Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
    //           .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //       roles.add(adminRole);

    //       break;
    //     case "mod":
    //       Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
    //           .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //       roles.add(modRole);

    //       break;
    //     default:
    //       Role userRole = roleRepository.findByName(ERole.ROLE_USER)
    //           .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //       roles.add(userRole);
    //     }
    //   });
    // }

    user.setRoles(Set.of(roleRepository.findByName(ERole.ROLE_USER).get()));
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
