package dev.findfirst.core.dto;

import java.util.List;

public record TagDTO(long id,  String title,  List<BookmarkDTO> bookmarks) {

  // @Override
  // public final String toString() {
  //   String bookmarksString = "";
  //
  //   for(var bk: bookmarks) { 
  //     bookmarksString = bookmarksString + bk.toString() + "\n";
  //   }
  //
  //   return "ID: %s, title: %s, bookmarks: %s".formatted(id, title, bookmarksString);
  // }

}
