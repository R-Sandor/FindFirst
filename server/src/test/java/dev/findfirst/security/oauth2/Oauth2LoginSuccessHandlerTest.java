package dev.findfirst.security.oauth2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import dev.findfirst.security.jwt.JwtService;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.jwt.service.TokenService;
import dev.findfirst.security.oauth2client.handlers.Oauth2LoginSuccessHandler;
import dev.findfirst.security.userauth.models.RefreshToken;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.UserRepo;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Tests borrowed from:
 * https://github.com/vadof/vplay-backend/blob/15e355f9f2283feb389e55e75aa4f620b62becea/user-service/src/test/java/com/vcasino/user/oauth2/OAuth2LoginSuccessHandlerTests.java
 */
@ExtendWith(MockitoExtension.class)
class Oauth2LoginSuccessHandlerTest {

   private final int ID = 1111111111;
  private final String EMAIL = "johndoe@gmail.com";
  private final String NAME = "John Doe";
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
    authenticateUserByProvider("github");
  }


  private User getUserMock(String provider, boolean active) {
    return new User(USERNAME, EMAIL, null, true);
  }

  private void authenticateUserByProvider(String provider) throws Exception {
    OAuth2AuthenticationToken oAuthToken = mockAuthentication(provider.toString().toLowerCase(), ID).getFirst();

    User user = getUserMock(provider, true);

    Entry<List<Cookie>, String> cookiesAndUrl = mockAuthenticationCookies(user);

    MockHttpServletResponse response = new MockHttpServletResponse();
    oAuthHandler.onAuthenticationSuccess(new MockHttpServletRequest(), response, oAuthToken);

    checkCookies(cookiesAndUrl.getFirst(), response);

    assertEquals(cookiesAndUrl.getSecond(), response.getRedirectedUrl());
  }

  private Entry<List<Cookie>, String> mockAuthenticationCookies(User user) {
    String jwtToken = "AAA-BBB-CCC";
    var refreshToken = new RefreshToken(1l, null, "fasfsaf-asfsf", null);

    when(ts.generateTokenFromUser(ID)).thenReturn(jwtToken);

    when(rs.createRefreshToken(user)).thenReturn(refreshToken);

    Cookie jwtCookie = new Cookie("jwt", "AAA-BBB-CCC");
    Cookie refreshCookie = new Cookie("refresh", refreshToken.getToken());

    String expectedUrl = "%s/login/success?name=%s&username=%s&email=%s"
        .formatted("localhost", user.getUsername(), user.getUsername(), user.getEmail());

    return new Entry<>(List.of(jwtCookie, refreshCookie), expectedUrl);
  }

  private Entry<OAuth2AuthenticationToken, OAuth2User> mockAuthentication(String provider, int id, String email) {
    var entry = mockAuthentication(provider, id);
    when(entry.getFirst().getPrincipal()).thenReturn(entry.getSecond());
    when(entry.getSecond().getAttribute("email")).thenReturn(email);
    return entry;
  }

  private Entry<OAuth2AuthenticationToken, OAuth2User> mockAuthentication(String provider, int id) {
    OAuth2AuthenticationToken oauthToken = mock(OAuth2AuthenticationToken.class);
    OAuth2User principal = mock(OAuth2User.class);
    when(oauthToken.getAuthorizedClientRegistrationId()).thenReturn(provider);
    when(oauthToken.getName()).thenReturn(id + "");
    return new Entry<>(oauthToken, principal);
  }

  private void checkCookies(List<Cookie> expectedCookies, MockHttpServletResponse response) {
    if (expectedCookies != null && !expectedCookies.isEmpty()) {
      for (Cookie expectedCookie : expectedCookies) {
        Cookie actualCookie = response.getCookie(expectedCookie.getName());
        assertNotNull(actualCookie);
        assertEquals(expectedCookie.getValue(), actualCookie.getValue());
      }
    }
  }

  @Getter
  @AllArgsConstructor
  static class Entry<F, S> {
    F first;
    S second;
  }

}
