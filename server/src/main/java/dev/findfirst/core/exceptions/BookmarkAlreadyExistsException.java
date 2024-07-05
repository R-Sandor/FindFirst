package dev.findfirst.core.exceptions;

public class BookmarkAlreadyExistsException extends Exception {
  public BookmarkAlreadyExistsException() {
    super("Bookmark already exists with the same data.");
  }
}
