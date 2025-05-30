package dev.findfirst.security.jwt.service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import dev.findfirst.users.repository.UserRepo;
import dev.findfirst.users.model.user.URole;
import dev.findfirst.security.userauth.utils.Constants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

  @Value("${findfirst.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${jwt.public.key}")
  private RSAPublicKey key;

  @Value("${jwt.private.key}")
  private RSAPrivateKey priv;

  private final UserRepo userRepo;


  public String generateTokenFromUser(int userId) {
    Instant now = Instant.now();
    var user = userRepo.findById(userId).orElseThrow();
    String email = user.getEmail();
    Integer roleId = user.getRole().getId();
    var roleName = URole.values()[roleId].toString();
    JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
        .expiresAt(now.plusSeconds(jwtExpirationMs)).subject(email).claim("scope", email)
        .claim(Constants.ROLE_ID_CLAIM, roleId).claim(Constants.ROLE_NAME_CLAIM, roleName)
        .claim("userId", userId).build();
    return jwtEncoder().encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }


  JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  // private String extractUserId(Authentication authentication) {
  // if (authentication.getPrincipal() instanceof UserDetails) {
  // String details = ((UserDetails) authentication.getPrincipal()).getUsername();
  // System.out.println("If details " + details);
  // return details;
  // } else if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
  // DefaultOAuth2User oAuth2User = (DefaultOAuth2User)
  // authentication.getPrincipal();
  // String details = oAuth2User.getAttribute("id");
  // System.out.println("Else details " + details);
  // return details;
  // }
  // return null;
  // }
}
