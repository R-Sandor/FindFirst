package dev.findfirst.users.exceptions;

public class UserNameTakenException extends Exception {
  public UserNameTakenException() {
    super("User name is already taken");
  }
}
