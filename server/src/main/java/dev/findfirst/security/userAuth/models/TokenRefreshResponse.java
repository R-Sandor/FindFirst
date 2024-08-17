package dev.findfirst.security.userAuth.models;

public record TokenRefreshResponse(String tokenType, String refreshToken) {
	public TokenRefreshResponse(String refreshToken) {
		this("Bearer", refreshToken);
	}
}
