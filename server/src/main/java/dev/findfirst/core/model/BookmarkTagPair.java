package dev.findfirst.core.model;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;
import dev.findfirst.core.model.jdbc.TagJDBC;

public record BookmarkTagPair(BookmarkJDBC bkmk, TagJDBC tag) {
}
