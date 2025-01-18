package dev.findfirst.core.exceptions;

import org.springframework.http.HttpStatus;

public class PageGreaterThanTotalException extends ErrorResponseException {
  public PageGreaterThanTotalException(int page) {
    super(HttpStatus.BAD_REQUEST, "Page parameter greater than total pages",
        "Requested page " + page + " exceeds total pages.");
  }
}
