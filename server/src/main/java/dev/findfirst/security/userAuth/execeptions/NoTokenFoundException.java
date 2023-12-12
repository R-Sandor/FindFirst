package dev.findfirst.security.userAuth.execeptions;

public class NoTokenFoundException extends Exception {
  public NoTokenFoundException() {
    super("No Token Found");
  }
}
