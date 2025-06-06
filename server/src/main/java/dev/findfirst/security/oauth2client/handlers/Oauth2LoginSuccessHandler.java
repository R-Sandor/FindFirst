package dev.findfirst.security.oauth2client.handlers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.jwt.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Value("${findfirst.app.frontend-url}")
  private String redirectURL;

  @Value("${findfirst.app.domain}")
  private String domain;

  @Value("${findfirst.secure-cookies:true}")
  private boolean secure;

  private final TokenService ts;
  private final RefreshTokenService rt;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {

    DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
    var userID = (Integer) principal.getAttributes().get("userID");

    var jwt = ts.generateTokenFromUser(userID);

    ResponseCookie cookie = ResponseCookie.from("findfirst", jwt).secure(secure).path("/")
        .domain(domain).httpOnly(true).build();

    response.addHeader("Set-Cookie", cookie.toString());
    response.getWriter().write("""
          { refreshToken: %s}
        """.formatted(rt.createRefreshToken(userID)));
    getRedirectStrategy().sendRedirect(request, response, redirectURL + "/account/login/oauth2");

  }

}
