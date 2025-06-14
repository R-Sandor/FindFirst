package dev.findfirst.security.oauth2;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.findfirst.security.jwt.JwtService;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.jwt.service.TokenService;
import dev.findfirst.security.oauth2client.handlers.Oauth2LoginSuccessHandler;
import dev.findfirst.users.repository.UserRepo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.TestPropertySource;

/**
 * _Found using github search._ Test borrowed from:
 * https://github.com/vadof/vplay-backend/blob/15e355f9f2283feb389e55e75aa4f620b62becea/user-service/src/test/java/com/vcasino/user/oauth2/OAuth2LoginSuccessHandlerTests.java
 */
@ExtendWith(MockitoExtension.class)
class Oauth2LoginSuccessHandlerTest {

  private final int ID = 1111111111;
  private final String USERNAME = "johndoe";

  @Mock
  private UserRepo userRepo;

  @Mock
  private JwtService jwtService;

  @Mock
  private TokenService ts;

  @Mock
  private RefreshTokenService rs;

  @InjectMocks
  private Oauth2LoginSuccessHandler oAuthHandler;

  @Test
  @DisplayName("Authenticate active Github user")
  void authenticateGithubUser() throws Exception {
    oAuthHandler.setRedirectURL("localhost");
    authenticateUserByProvider("github");
  }


  private void authenticateUserByProvider(String provider) throws Exception {
    OAuth2AuthenticationToken oAuthToken =
        mockAuthentication(provider.toString().toLowerCase(), ID).getFirst();

    MockHttpServletResponse response = new MockHttpServletResponse();
    oAuthHandler.onAuthenticationSuccess(new MockHttpServletRequest(), response, oAuthToken);

    assertTrue(response.getHeader("Set-Cookie") != null);
    assertEquals("localhost/account/login/oauth2", response.getRedirectedUrl());
  }


  private Entry<OAuth2AuthenticationToken, OAuth2User> mockAuthentication(String provider, int id) {
    OAuth2AuthenticationToken oauthToken = mock(OAuth2AuthenticationToken.class);
    OAuth2User principal = mock(OAuth2User.class);
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("name", USERNAME);
    attributes.put("userID", ID);
    Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    OAuth2User user = new DefaultOAuth2User(authorities, attributes, "name");
    when(oauthToken.getPrincipal()).thenReturn(user);
    return new Entry<>(oauthToken, principal);
  }

  @Getter
  @AllArgsConstructor
  static class Entry<F, S> {
    F first;
    S second;
  }

}
