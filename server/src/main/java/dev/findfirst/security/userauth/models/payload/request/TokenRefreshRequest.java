package dev.findfirst.security.userauth.models.payload.request;

import lombok.NonNull;

public record TokenRefreshRequest(@NonNull String refreshToken) {
}
