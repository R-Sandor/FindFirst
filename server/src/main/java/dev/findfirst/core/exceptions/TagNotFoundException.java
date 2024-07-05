package dev.findfirst.core.exceptions;

public class TagNotFoundException extends Exception {
  public TagNotFoundException() {
    super("Tag not found.");
  }
}
