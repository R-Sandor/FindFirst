package dev.findfirst.core.model;

import jakarta.validation.constraints.NotBlank;

public record SearchBkmkByTitleReq(@NotBlank String title) {
}
