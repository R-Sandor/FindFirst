package dev.findfirst.core.dto;

import java.util.List;

public record PaginatedBookmarkRes(List<BookmarkDTO> bookmarks, Integer totalPages, Integer currentPage) {
}
