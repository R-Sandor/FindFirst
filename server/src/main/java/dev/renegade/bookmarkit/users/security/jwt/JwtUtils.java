package dev.renegade.bookmarkit.users.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${jwt.private.key}") private RSAPrivateKey priv;
  @Value("${bookmarkit.app.jwtCookieName}") private String jwtCookie;

  @Autowired JwtDecoder jwtDecoder;

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
      Map<String,Object> claims =  jwtDecoder.decode(authToken).getClaims();
      if (claims.get("sub") != null)
        return true;
    return false;
  }

}
