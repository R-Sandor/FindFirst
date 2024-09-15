package dev.findfirst.security.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${jwt.private.key}") private RSAPrivateKey priv;

  @Value("${findfirst.app.jwtCookieName}") private String jwtCookie;

  private final JwtDecoder jwtDecoder;

  private JwtParser jwtParser;

  @PostConstruct
  private void init() {
    // secretKey =
    // Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
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

  public Jws<Claims> parseJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException,
      MalformedJwtException, SignatureException, IllegalArgumentException {
    return jwtParser.parseClaimsJws(jwt);
  }

  public String getUserNameFromJwtToken(String token) {
    return jwtDecoder.decode(token).getClaimAsString("sub");
  }

  public boolean validateJwtToken(String authToken) {
    Map<String, Object> claims = jwtDecoder.decode(authToken).getClaims();
    if (claims.get("sub") != null)
      return true;
    return false;
  }
}
