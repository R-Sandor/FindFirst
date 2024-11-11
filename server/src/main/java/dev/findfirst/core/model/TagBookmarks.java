package dev.findfirst.core.model;

import java.util.List;

import dev.findfirst.core.dto.BookmarkOnly;

public record TagBookmarks(String tagTitle, List<BookmarkOnly> bookmarks) {
}
