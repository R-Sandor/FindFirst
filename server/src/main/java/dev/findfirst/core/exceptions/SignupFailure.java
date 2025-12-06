package dev.findfirst.core.exceptions;

public class SignupFailure extends RuntimeException {
  public SignupFailure(Exception e) {
    super("Signup failed", e);
  }

  public SignupFailure(String msg) {
    super(msg);
  }
}
