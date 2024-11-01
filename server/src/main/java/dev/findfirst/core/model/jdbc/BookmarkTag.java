package dev.findfirst.core.model.jdbc;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("bookmark_tag")
public class BookmarkTag {
  private Long bookmarkId;
  private Long tagId;
}
