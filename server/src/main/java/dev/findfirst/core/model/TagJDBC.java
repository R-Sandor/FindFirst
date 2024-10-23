package dev.findfirst.core.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("tag")
public record TagJDBC(
  @Id long id,
  Integer tenantId, 
  Date createdDate,
  String createdBy,
  String lastModifiedBy, 
  Date lastModifiedDate, 
  String title, 
  String url, 
  String screenshotUrl, 
  boolean scrapable, 
  @MappedCollection(idColumn = "tag_id")
  Set<BookmarkTag> bookmarks
) {
}
