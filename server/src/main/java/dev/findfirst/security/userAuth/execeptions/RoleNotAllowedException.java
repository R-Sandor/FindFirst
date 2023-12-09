package dev.findfirst.security.userAuth.execeptions;

public class RoleNotAllowedException extends RuntimeException {
  public RoleNotAllowedException(String msg) {
    super(msg);
  }
}
