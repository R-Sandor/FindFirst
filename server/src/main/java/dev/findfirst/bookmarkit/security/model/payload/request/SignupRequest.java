package dev.findfirst.bookmarkit.security.model.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank @Size(min = 3, max = 30) String username,
    @NotBlank @Size(max = 50) @Email String email,
    @NotBlank @Size(max = 50) String name,
    @NotBlank @Size(min = 6, max = 40) String password) {}
