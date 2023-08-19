package dev.findfirst.bookmarkit.security.jwt;

import dev.findfirst.bookmarkit.security.utils.Constants;
import dev.findfirst.bookmarkit.users.model.user.User;
import dev.findfirst.bookmarkit.users.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${jwt.private.key}") private RSAPrivateKey priv;

  @Value("${bookmarkit.app.jwtCookieName}") private String jwtCookie;

  @Value("${bookmarkit.app.jwtExpirationMs}") private int jwtExpirationMs;

  @Autowired JwtEncoder encoder;
  @Autowired JwtDecoder jwtDecoder;
  @Autowired UserService userService;

  private JwtParser jwtParser;

  @PostConstruct
  private void init() {
    // secretKey = Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
    jwtParser = Jwts.parserBuilder().setSigningKey(priv).build();
  }

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public Jws<Claims> parseJwt(String jwt)
      throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
          SignatureException, IllegalArgumentException {
    return jwtParser.parseClaimsJws(jwt);
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
    return this.generateTokenFromUser(userService.getUserByEmail(username));
  }

  public String generateTokenFromUser(User user) {
    Instant now = Instant.now();
    String email = user.getEmail();
    Integer roleId = user.getRole().getId();
    String roleName = user.getRole().getName().name();
    Integer tenantId = user.getTenantId();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(Instant.now())
            .expiresAt(now.plusSeconds(jwtExpirationMs))
            .subject(email)
            .claim("scope", email)
            .claim(Constants.ROLE_ID_CLAIM, roleId)
            .claim(Constants.ROLE_NAME_CLAIM, roleName)
            .claim(Constants.TENANT_ID_CLAIM, tenantId)
            .build();
    return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
