package dev.findfirst.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import dev.findfirst.security.conditions.OAuthClientsCondition;
import dev.findfirst.security.oauth2client.OauthUserService;
import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.service.UserManagementService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class Oauth2BeanConfig {

  final UserManagementService ums;
  final UserRepo userRepo;

  @Conditional(OAuthClientsCondition.class)
  @Bean
  public OauthUserService oauthUserService() {
    return new OauthUserService(userRepo, ums, new DefaultOAuth2UserService());
  }

}
