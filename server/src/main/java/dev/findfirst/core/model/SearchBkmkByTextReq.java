package dev.findfirst.core.model;

import jakarta.validation.constraints.NotBlank;

public record SearchBkmkByTextReq(@NotBlank String text) {
}
