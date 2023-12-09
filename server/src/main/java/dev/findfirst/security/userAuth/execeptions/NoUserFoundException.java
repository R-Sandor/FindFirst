package dev.findfirst.security.userAuth.execeptions;

public class NoUserFoundException extends RuntimeException {
  public NoUserFoundException() {
    super("No user found");
  }
}
