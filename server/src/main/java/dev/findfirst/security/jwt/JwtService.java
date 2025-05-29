package dev.findfirst.security.jwt;

import java.security.interfaces.RSAPrivateKey;
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

  @Value("${findfirst.app.jwtCookieName}")
  private String jwtCookie;

  private final JwtDecoder jwtDecoder;

  private JwtParser jwtParser;

  @PostConstruct
  private void init() {
    jwtParser = Jwts.parserBuilder().setSigningKey(priv).build();
  }

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    return cookie != null ? cookie.getValue() : null;
  }

  public Jws<Claims> parseJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException,
      MalformedJwtException, SignatureException, IllegalArgumentException {
    return jwtParser.parseClaimsJws(jwt);
  }


  public boolean validateJwtToken(String authToken) {
    Map<String, Object> claims = jwtDecoder.decode(authToken).getClaims();
    return claims.get("sub") != null;
  }
}
