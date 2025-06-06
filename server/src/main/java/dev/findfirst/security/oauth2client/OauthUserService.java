package dev.findfirst.security.oauth2client;

import java.rmi.UnexpectedException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dev.findfirst.security.userauth.models.payload.request.SignupRequest;
import dev.findfirst.users.exceptions.EmailAlreadyRegisteredException;
import dev.findfirst.users.exceptions.UserNameTakenException;
import dev.findfirst.users.model.user.URole;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.service.UserManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  final UserRepo userRepo;
  final UserManagementService ums;

  @Transactional
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
        new DefaultOAuth2UserService();
    OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
    User user = null;

    // user exists in database by email
    var attrs = oAuth2User.getAttributes();
    var email = (String) attrs.get("email");
    var username = (String) attrs.get("login");
    String registrationId = userRequest.getClientRegistration().getClientId();
    if (email != null && !email.isEmpty()) {
      log.debug("attempt login with email {}", email);
      // user = userRepo.findByEmail(email).or()
    } else if (username != null && !username.isEmpty()) {
      log.debug("looking up if user exist with username {}", username);
      var userOpt = userRepo.findByUsername(username);

      var oauth2PlaceholderEmail = username + registrationId;
      if (userOpt.isEmpty()) {
        try {
          log.debug("creating a new user for oauth2");
          user = ums.createNewUserAccount(
              new SignupRequest(username, oauth2PlaceholderEmail, UUID.randomUUID().toString()));
        } catch (UnexpectedException | UserNameTakenException | EmailAlreadyRegisteredException e) {
          log.debug("errors occured: {}", e.getMessage());
        }
      } else {
        user = userOpt.get();
      }
    }
    if (user.getUserId() != null) {
      GrantedAuthority authority =
          new SimpleGrantedAuthority(URole.values()[user.getRole().getId()].toString());
      String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
          .getUserInfoEndpoint().getUserNameAttributeName();
      log.debug("USER ATTRIBUTE NAME: {}", userNameAttributeName);
      var attributes =
          customAttribute(attrs, userNameAttributeName, user.getUserId(), registrationId);
      return new DefaultOAuth2User(Collections.singletonList(authority), attributes,
          userNameAttributeName);
    }

    return oAuth2User;
  }

  private Map<String, Object> customAttribute(Map<String, Object> attributes,
      String userNameAttributeName, int userID, String registrationId) {
    Map<String, Object> customAttribute = new HashMap<>();
    customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
    customAttribute.put("provider", registrationId);
    customAttribute.put("userID", userID);
    return customAttribute;
  }

}
