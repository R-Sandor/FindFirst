package dev.findfirst.core.model.jdbc;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookmarkTagID implements Serializable {
  private Long bookmarkId;
  private Long TagId;
}
