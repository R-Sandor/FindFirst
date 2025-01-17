package dev.findfirst.core.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PaginatedBookmarkReq(
        @Min(1) Integer page,
        @Min(6) @Max(25) Integer size
) {
}
