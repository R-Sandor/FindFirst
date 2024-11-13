package dev.findfirst.core.model.jdbc;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("bookmark")
public class BookmarkJDBC {

  @Id
  private Long id;

  private Integer userId;
  private Date createdDate;
  private String createdBy;
  private String lastModifiedBy;
  private Date lastModifiedDate;
  private String title;
  private String url;
  private String screenshotUrl;
  private Boolean scrapable;

  @MappedCollection(idColumn = "bookmark_id")
  Set<BookmarkTag> tags = new HashSet<>();

  public Set<BookmarkTag> addTag(BookmarkTag bookmarkTag) {
    tags.add(new BookmarkTag(bookmarkTag.getBookmarkId(), bookmarkTag.getTagId()));
    return new HashSet<>(tags);
  }


}
