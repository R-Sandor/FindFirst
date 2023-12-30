package dev.findfirst.users.exceptions;

public class NoUserFoundException extends Exception {
  public NoUserFoundException() {
    super("No user found");
  }
}
