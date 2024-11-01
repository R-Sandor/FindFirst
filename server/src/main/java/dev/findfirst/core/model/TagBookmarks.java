package dev.findfirst.core.model;

import java.util.List;

import dev.findfirst.core.model.jpa.Bookmark;

public record TagBookmarks(String tagTitle, List<Bookmark> bookmarks) {
}
