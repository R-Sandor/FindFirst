package dev.findfirst.security.model.payload.request;

public record TokenRefreshRequest(String refreshToken) {
  public TokenRefreshRequest {
    if (refreshToken.isBlank()) {
      throw new IllegalArgumentException("Refresh token must not be null or blank.");
    }
  }
}
