package dev.findfirst.core.dto;

import java.util.List;

public record TagDTO(long id,  String title,  List<BookmarkDTO> bookmarks) {

}
