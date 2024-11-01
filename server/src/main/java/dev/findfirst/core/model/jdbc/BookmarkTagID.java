package dev.findfirst.core.model.jdbc;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Embeddable
public class BookmarkTagID implements Serializable {
  private Long bookmarkId;
  private Long TagId;
}
