package dev.findfirst.security.jwt.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import dev.findfirst.security.jwt.exceptions.TokenRefreshException;
import dev.findfirst.security.jwt.repo.RefreshTokenRepository;
import dev.findfirst.security.userauth.models.RefreshToken;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  @Value("${findfirst.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserRepo userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(User user) {
    return createRefreshToken(user.getUserId());
  }

  public RefreshToken createRefreshToken(int userID) {
    RefreshToken refreshToken = new RefreshToken(null, AggregateReference.to(userID),
        UUID.randomUUID().toString(), Instant.now().plusMillis(refreshTokenDurationMs));

    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(),
          "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Integer userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow());
  }
}
