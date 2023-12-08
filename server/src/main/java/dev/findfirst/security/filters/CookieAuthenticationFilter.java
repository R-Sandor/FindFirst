package dev.findfirst.security.filters;

import dev.findfirst.security.jwt.JwtService;
import dev.findfirst.security.jwt.TenantAuthenticationToken;
import dev.findfirst.security.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class CookieAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(CookieAuthenticationFilter.class);

  @Autowired private JwtService jwtUtils;

  private String parseJwt(HttpServletRequest request) {
    return jwtUtils.getJwtFromCookies(request);
  }

  private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Jws<Claims> jwsClaims) {
    String role = jwsClaims.getBody().get(Constants.ROLE_NAME_CLAIM, String.class);
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
    return Collections.singletonList(simpleGrantedAuthority);
  }

  private TenantAuthenticationToken getTenantAuthenticationToken(String jwt) {
    TenantAuthenticationToken tenantAuthenticationToken = null;

    try {
      /* AUTHENTICATION */
      Jws<Claims> jwsClaims = jwtUtils.parseJwt(jwt);
      String email = jwsClaims.getBody().getSubject();
      /* AUTHORIZATION */
      int roleId = jwsClaims.getBody().get(Constants.ROLE_ID_CLAIM, Integer.class);
      List<SimpleGrantedAuthority> authorities = getSimpleGrantedAuthorities(jwsClaims);
      int tenantId = jwsClaims.getBody().get(Constants.TENANT_ID_CLAIM, Integer.class);
      tenantAuthenticationToken =
          new TenantAuthenticationToken(email, roleId, authorities, tenantId);
    } catch (ExpiredJwtException
        | UnsupportedJwtException
        | MalformedJwtException
        | SignatureException
        | IllegalArgumentException e) {
      logger.error("Problems with JWT", e);
    }

    return tenantAuthenticationToken;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        TenantAuthenticationToken tenantAuthenticationToken = getTenantAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(tenantAuthenticationToken);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }
}
