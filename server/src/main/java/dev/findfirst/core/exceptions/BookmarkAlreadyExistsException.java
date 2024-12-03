package dev.findfirst.core.exceptions;

import org.springframework.http.HttpStatus;

public class BookmarkAlreadyExistsException extends ErrorResponseException {
  public BookmarkAlreadyExistsException(String logMessage) {
    super(HttpStatus.CONFLICT, "Bookmark already exists.", logMessage);
  }
}
