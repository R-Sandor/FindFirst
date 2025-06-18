package dev.findfirst.security.oauth2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.rmi.UnexpectedException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dev.findfirst.security.oauth2client.OauthUserService;
import dev.findfirst.users.exceptions.EmailAlreadyRegisteredException;
import dev.findfirst.users.exceptions.UserNameTakenException;
import dev.findfirst.users.model.user.Role;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.service.UserManagementService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.mapping.AggregateReference.IdOnlyAggregateReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * This code tests the Oauth2 _Test Code borrowed from_:
 * https://github.com/i-moonlight/Suricate/blob/8fe4f0f87b2c8ecfefc881efc370f3eae5c97209/src/test/java/com/michelin/suricate/security/oauth2/Oauth2UserServiceTest.java#L60
 * 
 */
@ExtendWith(MockitoExtension.class)
public class OauthUserServiceTest {

  @Mock
  UserRepo userRepo;
  @Mock
  UserManagementService ums;

  @Mock
  OAuth2UserRequest oAuth2UserRequest;

  @Mock
  DefaultOAuth2UserService defaultOAuth2UserService;

  @Mock
  private OAuth2User oAuth2User;

  @InjectMocks
  private OauthUserService oAuthService;

  @Test
  void userAlreadyExistByEmail() {
    final String username = "jsmith";
    final String email = "jsmith@gmail.com";

    OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token",
        Instant.now(), Instant.now().plusSeconds(3600));

    ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("gmail")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).clientId("clientId")
        .clientSecret("clientSecret").redirectUri("localhost:8080")
        .authorizationUri("localhost:8080/authorizationUri").tokenUri("localhost:8080/tokenUri")
        .userInfoUri("http://localhost:8080/userInfoUri").userNameAttributeName("username").build();
    OAuth2UserRequest request = new OAuth2UserRequest(clientRegistration, token);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("email", email);
    attributes.put("username", username);

    when(defaultOAuth2UserService.loadUser(request)).thenReturn(oAuth2User);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(userRepo.findByEmail(email)).thenReturn(Optional.of(User.builder().username(username)
        .email(email).role(new IdOnlyAggregateReference<Role, Integer>(0)).userId(1).build()));
    var oauth2User = oAuthService.loadUser(request);

    assertEquals(username, oauth2User.getAttribute("username"));
  }

  @Test
  void userAlreadyExistByUsername() {
    final String username = "jsmith";

    OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token",
        Instant.now(), Instant.now().plusSeconds(3600));

    ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).clientId("clientId")
        .clientSecret("clientSecret").redirectUri("localhost:8080")
        .authorizationUri("localhost:8080/authorizationUri").tokenUri("localhost:8080/tokenUri")
        .userInfoUri("http://localhost:8080/userInfoUri").userNameAttributeName("login").build();
    OAuth2UserRequest request = new OAuth2UserRequest(clientRegistration, token);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login", username);

    when(defaultOAuth2UserService.loadUser(request)).thenReturn(oAuth2User);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(userRepo.findByUsername(username)).thenReturn(Optional
        .of(User.builder().username(username).email("generated-github-jsmith@noemail.invalid")
            .role(new IdOnlyAggregateReference<Role, Integer>(0)).userId(1).build()));
    var oauth2User = oAuthService.loadUser(request);

    assertEquals(username, oauth2User.getAttribute("login"));
  }

  @Test
  void hasUsernameFromOauthNoEmailButDoesNotExistAsUserAccountYet()
      throws UnexpectedException, UserNameTakenException, EmailAlreadyRegisteredException {
    final String username = "jsmith";

    OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token",
        Instant.now(), Instant.now().plusSeconds(3600));

    ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).clientId("clientId")
        .clientSecret("clientSecret").redirectUri("localhost:8080")
        .authorizationUri("localhost:8080/authorizationUri").tokenUri("localhost:8080/tokenUri")
        .userInfoUri("http://localhost:8080/userInfoUri").userNameAttributeName("login").build();
    OAuth2UserRequest request = new OAuth2UserRequest(clientRegistration, token);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login", username);

    when(defaultOAuth2UserService.loadUser(request)).thenReturn(oAuth2User);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(userRepo.findByUsername(username)).thenReturn(Optional.ofNullable(null));
    when(ums.createNewUserAccount(any())).thenReturn(
        User.builder().username(username).email("generated-github-jsmith@noemail.invalid")
            .role(new IdOnlyAggregateReference<Role, Integer>(0)).userId(1).build());
    var oauth2User = oAuthService.loadUser(request);

    assertEquals(username, oauth2User.getAttribute("login"));
  }

  @Test
  void hasUsernameFromOauthEmailButDoesNotExistAsUserAccountYet()
      throws UnexpectedException, UserNameTakenException, EmailAlreadyRegisteredException {
    final String username = "jsmith";
    final String email = "jsmith@gmail.com";

    OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token",
        Instant.now(), Instant.now().plusSeconds(3600));

    ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).clientId("clientId")
        .clientSecret("clientSecret").redirectUri("localhost:8080")
        .authorizationUri("localhost:8080/authorizationUri").tokenUri("localhost:8080/tokenUri")
        .userInfoUri("http://localhost:8080/userInfoUri").userNameAttributeName("username").build();
    OAuth2UserRequest request = new OAuth2UserRequest(clientRegistration, token);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login", username);
    attributes.put("email", email);

    when(defaultOAuth2UserService.loadUser(request)).thenReturn(oAuth2User);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    when(userRepo.findByEmail(email)).thenReturn(Optional.ofNullable(null));
    when(ums.createNewUserAccount(any()))
        .thenReturn(User.builder().username(username).email("jsmith@gmail.com")
            .role(new IdOnlyAggregateReference<Role, Integer>(0)).userId(1).build());
    var oauth2User = oAuthService.loadUser(request);

    assertEquals(username, oauth2User.getAttribute("username"));
  }

  @Test
  void strangeSignupError()
      throws UnexpectedException, UserNameTakenException, EmailAlreadyRegisteredException {
    final String username = "jsmith";
    final String email = "jsmith@gmail.com";

    OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "token",
        Instant.now(), Instant.now().plusSeconds(3600));

    ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("github")
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).clientId("clientId")
        .clientSecret("clientSecret").redirectUri("localhost:8080")
        .authorizationUri("localhost:8080/authorizationUri").tokenUri("localhost:8080/tokenUri")
        .userInfoUri("http://localhost:8080/userInfoUri").userNameAttributeName("username").build();
    OAuth2UserRequest request = new OAuth2UserRequest(clientRegistration, token);

    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login", username);
    attributes.put("email", email);

    when(defaultOAuth2UserService.loadUser(request)).thenReturn(oAuth2User);
    when(oAuth2User.getAttributes()).thenReturn(attributes);
    // when(userRepo.findByEmail(email)).thenReturn(Optional.ofNullable(null));
    when(ums.createNewUserAccount(any())).thenThrow(new UserNameTakenException());

    assertThrows(RuntimeException.class, () -> {
      oAuthService.loadUser(request);
    });

  }

}
