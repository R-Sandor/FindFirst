package dev.findfirst.core.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBookmarkReq(long id, @NotBlank String title, Boolean isScrapable) {
}
