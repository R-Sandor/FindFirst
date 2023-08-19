package dev.findfirst.bookmarkit.security.execeptions;

public class RoleNotAllowedException extends RuntimeException {
  public RoleNotAllowedException(String msg) {
    super(msg);
  }
}
