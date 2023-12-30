package dev.findfirst.users.exceptions;

public class NoTokenFoundException extends Exception {
  public NoTokenFoundException() {
    super("No Token Found");
  }
}
