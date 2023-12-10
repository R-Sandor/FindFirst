package dev.findfirst.security.userAuth.execeptions;

public class TokenExpiredException extends Exception {
  public TokenExpiredException() {
    super("Token expired");
  }
}
