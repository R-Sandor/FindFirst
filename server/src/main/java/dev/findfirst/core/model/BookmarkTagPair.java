package dev.findfirst.core.model;

import dev.findfirst.core.model.jpa.Bookmark;
import dev.findfirst.core.model.jpa.Tag;

public record BookmarkTagPair(Bookmark bkmk, Tag tag) {
}
