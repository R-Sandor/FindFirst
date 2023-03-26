package dev.findfirst.bookmarkit.security.model.payload;

public record TokenRefreshResponse(String tokenType, String refreshToken) {
  public TokenRefreshResponse(String refreshToken) {
    this("Bearer", refreshToken);
  }
}
