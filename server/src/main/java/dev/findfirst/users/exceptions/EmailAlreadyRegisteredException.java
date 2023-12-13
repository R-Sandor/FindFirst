package dev.findfirst.users.exceptions;

public class EmailAlreadyRegisteredException extends Exception {
  public EmailAlreadyRegisteredException() {
    super("User name is already taken");
  }
}
