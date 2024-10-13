package dev.findfirst.core.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("bookmark")
@Data
public class BookmarkJDBC {

  @Id
  Long id;

  private Integer tenantId;
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

}
