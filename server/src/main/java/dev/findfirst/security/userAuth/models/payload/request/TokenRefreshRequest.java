package dev.findfirst.security.userAuth.models.payload.request;

import lombok.NonNull;

public record TokenRefreshRequest(@NonNull String refreshToken) {
}
