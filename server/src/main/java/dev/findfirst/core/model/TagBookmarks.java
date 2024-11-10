package dev.findfirst.core.model;

import java.util.List;

import dev.findfirst.core.dto.BookmarkDTO;

public record TagBookmarks(String tagTitle, List<BookmarkDTO> bookmarks) {
}
