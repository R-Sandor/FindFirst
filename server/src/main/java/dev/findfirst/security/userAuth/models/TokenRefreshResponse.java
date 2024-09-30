package dev.findfirst.security.userAuth.models;

public record TokenRefreshResponse(String tokenType, String refreshToken, String error) {
  public TokenRefreshResponse(String refreshToken) {
		this("Bearer", refreshToken, null);
	}
}
