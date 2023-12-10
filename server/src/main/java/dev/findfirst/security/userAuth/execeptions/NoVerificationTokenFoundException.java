package dev.findfirst.security.userAuth.execeptions;

public class NoVerificationTokenFoundException extends Exception {
  public NoVerificationTokenFoundException() {
    super("No Token Found");
  }
}
