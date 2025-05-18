package dev.findfirst.security.oauth2client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.rmi.UnexpectedException;
import java.util.UUID;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.findfirst.security.userauth.models.payload.request.SignupRequest;
import dev.findfirst.users.exceptions.EmailAlreadyRegisteredException;
import dev.findfirst.users.exceptions.UserNameTakenException;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.service.UserManagementService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  final UserRepo userRepo;
  final UserManagementService ums;

  @Transactional
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

    // user exists in database by email
    var attrs = oAuth2User.getAttributes();
    var email = (String) attrs.get("email");
    var username = (String) attrs.get("login");
    if (email != null && !email.isEmpty()) {
      log.debug("attempt login with email {}", email);
    } else if (username != null && !username.isEmpty()) {
      log.debug("looking up if user exist with username {}", username);
      var oauth2PlaceholderEmail = username + userRequest.getClientRegistration().getClientId();
      if (userRepo.findByUsername(username).isEmpty()) {
        try {
          ums.createNewUserAccount(new SignupRequest(username, oauth2PlaceholderEmail, UUID.randomUUID().toString()));
        } catch (UnexpectedException | UserNameTakenException | EmailAlreadyRegisteredException e) {
          // TODO Auto-generated catch block
          log.debug("errors occured");
        }
      }
    }

    return oAuth2User;
  }

}
