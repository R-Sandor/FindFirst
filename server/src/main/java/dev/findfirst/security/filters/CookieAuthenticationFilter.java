package dev.findfirst.security.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.findfirst.security.jwt.JwtService;
import dev.findfirst.security.jwt.UserAuthenticationToken;
import dev.findfirst.security.userauth.utils.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class CookieAuthenticationFilter extends OncePerRequestFilter {

  private JwtService jwtUtils;

  @Autowired
  public void setJwtUtils(JwtService jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  private String parseJwt(HttpServletRequest request) {
    return jwtUtils.getJwtFromCookies(request);
  }

  private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Jws<Claims> jwsClaims) {
    String role = jwsClaims.getPayload().get(Constants.ROLE_NAME_CLAIM, String.class);
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
    return Collections.singletonList(simpleGrantedAuthority);
  }

  private UserAuthenticationToken getUserAuthenticationToken(String jwt) {
    UserAuthenticationToken userAuthenticationToken = null;

    try {
      /* AUTHENTICATION */
      Jws<Claims> jwsClaims = jwtUtils.parseJwt(jwt);
      String email = jwsClaims.getBody().getSubject();
      /* AUTHORIZATION */
      int roleId = jwsClaims.getBody().get(Constants.ROLE_ID_CLAIM, Integer.class);
      List<SimpleGrantedAuthority> authorities = getSimpleGrantedAuthorities(jwsClaims);
      int userId = jwsClaims.getBody().get(Constants.USER_ID_CLAIM, Integer.class);
      userAuthenticationToken = new UserAuthenticationToken(email, roleId, authorities, userId);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
        | SignatureException | IllegalArgumentException e) {
      log.error("Problems with JWT", e);
    }

    return userAuthenticationToken;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        UserAuthenticationToken userAuthenticationToken = getUserAuthenticationToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(userAuthenticationToken);
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilterAsyncDispatch() {
    return false;
  }

  @Override
  protected boolean shouldNotFilterErrorDispatch() {
    return false;
  }
}
