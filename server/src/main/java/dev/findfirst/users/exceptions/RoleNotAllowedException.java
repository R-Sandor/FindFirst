package dev.findfirst.users.exceptions;

public class RoleNotAllowedException extends RuntimeException {
  public RoleNotAllowedException(String msg) {
    super(msg);
  }
}
