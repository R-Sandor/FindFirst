package dev.findfirst.security.oauth2client;

import java.rmi.UnexpectedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import dev.findfirst.core.exceptions.SignupFailure;
import dev.findfirst.security.conditions.OAuthClientsCondition;
import dev.findfirst.security.userauth.models.payload.request.SignupRequest;
import dev.findfirst.users.exceptions.EmailAlreadyRegisteredException;
import dev.findfirst.users.exceptions.UserNameTakenException;
import dev.findfirst.users.model.user.URole;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.service.UserManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Conditional(OAuthClientsCondition.class)
@Slf4j
@RequiredArgsConstructor
@Primary
public class OauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepo userRepo;

  private final UserManagementService ums;

  @Qualifier("defaultOauthService") private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate;

  @Transactional
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.debug("attempt to loadUser");
    OAuth2User oAuth2User = delegate.loadUser(userRequest);
    User user = null;

    // user exists in database by email
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();
    if (userRequest.getClientRegistration().getClientName().equalsIgnoreCase("github")) {
      log.debug("set the userNameAttributeName to login");
      userNameAttributeName = "login";
    }

    log.debug("userNameAttributeName {}", userNameAttributeName);
    final var attrs = oAuth2User.getAttributes();
    final var email = (String) attrs.get("email");

    var username = attrs.get(userNameAttributeName).toString();
    final var registrationId = userRequest.getClientRegistration().getClientId();
    final var oauth2PlaceholderEmail =
        "generated-" + username + registrationId + "@noemail.invalid";

    Supplier<User> signup = () -> {
      try {
        if (email != null && !email.isEmpty()) {
          return signupUser(email, email);
        } else {
          return signupUser(username, oauth2PlaceholderEmail);
        }
      } catch (UnexpectedException | UserNameTakenException | EmailAlreadyRegisteredException e) {
        log.error("User that doesn't exist tried to signup but the signup failed", e);
        throw new SignupFailure(e);

      }
    };

    // Oauth2 with email
    if (email != null && !email.isEmpty()) {
      log.debug("attempt login with email {}", email);
      user = getUserFromOpt(userRepo.findByEmail(email), signup);
    }
    // Oauth2 by username (github, etc.)
    else if (username != null && !username.isEmpty()) {
      log.debug("looking up if user exist with username {}", username);
      user = getUserFromOpt(userRepo.findByUsername(username), signup);
    }

    if (user == null) {
      throw new SignupFailure("Error with user signup/signin");
    }

    int userRole =
        (user.getRole() == null || user.getRole().getId() == null) ? 0 : user.getRole().getId();

    GrantedAuthority authority = new SimpleGrantedAuthority(URole.values()[userRole].toString());
    var attributes =
        customAttribute(attrs, userNameAttributeName, user.getUserId(), registrationId);

    return new DefaultOAuth2User(Collections.singletonList(authority), attributes,
        userNameAttributeName);
  }

  private Map<String, Object> customAttribute(Map<String, Object> attributes,
      String userNameAttributeName, int userID, String registrationId) {
    Map<String, Object> customAttribute = new HashMap<>();
    customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
    customAttribute.put("provider", registrationId);
    customAttribute.put("userID", userID);
    return customAttribute;
  }

  private User getUserFromOpt(Optional<User> userOpt, Supplier<User> signupUser) {
    return userOpt.isEmpty() ? signupUser.get() : userOpt.get();
  }

  private User signupUser(String username, String email)
      throws UnexpectedException, UserNameTakenException, EmailAlreadyRegisteredException {
    log.debug("creating a new user for oauth2");
    return ums
        .createNewUserAccount(new SignupRequest(username, email, UUID.randomUUID().toString()));
  }

}
