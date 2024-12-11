package dev.findfirst.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBookmarkReq(long id, @NotBlank String title, @Size(max = 512) String url,  Boolean isScrapable) {
}
