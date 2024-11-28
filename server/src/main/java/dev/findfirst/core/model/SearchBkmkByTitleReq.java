package dev.findfirst.core.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SearchBkmkByTitleReq(@NotEmpty String [] title) {
}
