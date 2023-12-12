package dev.findfirst.security.userAuth.execeptions;

public class NoUserFoundException extends Exception {
  public NoUserFoundException() {
    super("No user found");
  }
}
