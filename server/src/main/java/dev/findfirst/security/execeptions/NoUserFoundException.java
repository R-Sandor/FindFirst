package dev.findfirst.security.execeptions;

public class NoUserFoundException extends RuntimeException {
  public NoUserFoundException() {
    super("No user found");
  }
}
