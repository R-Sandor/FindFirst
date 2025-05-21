package dev.findfirst.security.oauth2client.handlers;



import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import dev.findfirst.security.jwt.service.TokenService;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import dev.findfirst.users.model.user.SigninTokens;
import dev.findfirst.users.service.UserManagementService;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Value("${findfirst.app.frontend-url}")
  private String redirectURL; 

  private final TokenService ts;
  private final RefreshTokenService rt; 

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {
    log.debug("successful login");
  // ResponseCookie cookie = ResponseCookie.from("findfirst", tkns.jwt()).secure(secure).path("/")
  //       .domain(domain).httpOnly(true).build();
  //
  //   return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
  //       .body(new TokenRefreshResponse(tkns.refreshToken()));
  authentication.getPrincipal();
  DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
  var userID = (Integer) principal.getAttributes().get("userID");
  log.debug("userID {}", userID);
  ts.generateTokenFromUser(userID);

     // var signinTokens = new SigninTokens(jwt, refreshToken.getToken());
    //
  
    getRedirectStrategy().sendRedirect(request, response, redirectURL);

  }

}
