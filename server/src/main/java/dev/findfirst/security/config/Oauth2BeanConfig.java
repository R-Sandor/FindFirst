package dev.findfirst.security.config;

import dev.findfirst.security.conditions.OAuthClientsCondition;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.jwt.service.TokenService;
import dev.findfirst.security.oauth2client.handlers.Oauth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
@RequiredArgsConstructor
public class Oauth2BeanConfig {

  private final TokenService ts;

  private final RefreshTokenService rt;

  @Bean
  @Conditional(OAuthClientsCondition.class)
  public Oauth2LoginSuccessHandler oauth2Success() {
    return new Oauth2LoginSuccessHandler(ts, rt);
  }

  @Bean(name = {"defaultOauthService", "defaultOAuth2UserService"})
  public OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService() {
    return new DefaultOAuth2UserService();
  }
}
