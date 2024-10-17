package dev.findfirst.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Data
@Table
public class BookmarkTag {
  @Id
  private Long id;
  private Long bookmarkId;
  private Long TagId;
}
