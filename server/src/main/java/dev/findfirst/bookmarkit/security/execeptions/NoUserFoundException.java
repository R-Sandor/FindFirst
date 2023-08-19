package dev.findfirst.bookmarkit.security.execeptions;

public class NoUserFoundException extends RuntimeException {
  public NoUserFoundException() {
    super("No user found");
  }
}
