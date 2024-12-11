package dev.findfirst.core.dto;

import jakarta.validation.constraints.Size;

public record UpdateBookmarkReq(long id,  @Size(max = 512) String title, @Size(max = 512) String url, Boolean isScrapable) {
}
