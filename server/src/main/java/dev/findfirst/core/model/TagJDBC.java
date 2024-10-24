package dev.findfirst.core.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("tag")
public record TagJDBC(
    @Id Long id,
    Integer tenantId,
    Date createdDate,
    String createdBy,
    String lastModifiedBy,
    Date lastModifiedDate,
    String title,
    @MappedCollection(idColumn = "tag_id") Set<BookmarkTag> bookmarks) {

  public TagJDBC(
      Integer tenantId,
      Date createdDate,
      String createdBy,
      String lastModifiedBy,
      Date lastModifiedDate,
      String title) {
    this(null, tenantId, createdDate, createdBy, lastModifiedBy, lastModifiedDate, title, new HashSet<>());

  }

}
