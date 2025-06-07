package dev.findfirst.security.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${jwt.private.key}")
  private RSAPrivateKey priv;
  @Value("${jwt.public.key}")
  private RSAPublicKey pubKey;

  @Value("${findfirst.app.jwtCookieName}")
  private String jwtCookie;

  private final JwtDecoder jwtDecoder;

  private JwtParser jwtParser;

  @PostConstruct
  private void init() {
    jwtParser = Jwts.parser().verifyWith(pubKey).build();
  }

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    return cookie != null ? cookie.getValue() : null;
  }

  public Jws<Claims> parseJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException,
      MalformedJwtException, SignatureException, IllegalArgumentException {
    return jwtParser.parseSignedClaims(jwt);
  }

  public String getUserNameFromJwtToken(String token) {
    return jwtDecoder.decode(token).getClaimAsString("sub");
  }

  public boolean validateJwtToken(String authToken) {
    Map<String, Object> claims = jwtDecoder.decode(authToken).getClaims();
    return claims.get("sub") != null;
  }
}
