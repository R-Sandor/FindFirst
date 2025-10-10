package dev.findfirst.core.model;

import java.util.Arrays;

import jakarta.validation.constraints.NotEmpty;

public record SearchBkmkByTitleReq(@NotEmpty String[] keywords) {

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SearchBkmkByTitleReq(String[] keywords)) {
      return Arrays.equals(this.keywords(), keywords);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(this.keywords());
  }

  @Override
  public String toString() {
    return Arrays.toString(this.keywords());
  }
}
