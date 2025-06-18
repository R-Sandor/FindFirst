package dev.findfirst.security.config;

import dev.findfirst.security.conditions.OAuthClientsCondition;
import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.jwt.service.TokenService;
import dev.findfirst.security.oauth2client.handlers.Oauth2LoginSuccessHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Oauth2BeanConfig {

  final TokenService ts;
  final RefreshTokenService rt;

  @Bean
  @Conditional(OAuthClientsCondition.class)
  public Oauth2LoginSuccessHandler oauth2Success() {
    return new Oauth2LoginSuccessHandler(ts, rt);
  }
}
