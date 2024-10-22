package dev.findfirst.core.model;

import jakarta.validation.constraints.NotBlank;

public record SearchBkmkByTagReq(@NotBlank String tag) {
}
