package dev.findfirst.bookmarkit.security.jwt;

import dev.findfirst.bookmarkit.users.model.User;
import dev.findfirst.bookmarkit.users.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${jwt.private.key}") private RSAPrivateKey priv;

  @Value("${bookmarkit.app.jwtCookieName}") private String jwtCookie;

  @Value("${bookmarkit.app.jwtExpirationMs}") private int jwtExpirationMs;

  @Autowired JwtEncoder encoder;
  @Autowired JwtDecoder jwtDecoder;
  @Autowired UserService userService;

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public String getUserNameFromJwtToken(String token) {
    return jwtDecoder.decode(token).getClaimAsString("sub");
  }

  public boolean validateJwtToken(String authToken) {
    Map<String, Object> claims = jwtDecoder.decode(authToken).getClaims();
    if (claims.get("sub") != null) return true;
    return false;
  }

  public String generateTokenFromUsername(String username) {
    Instant now = Instant.now();
    Optional<User> acct = userService.getUserByEmail(username);
    String scope = acct.isPresent() ? acct.get().getEmail() : "";
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(Instant.now())
            .expiresAt(now.plusSeconds(jwtExpirationMs))
            .subject(username)
            .claim("scope", scope)
            .build();
    return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
