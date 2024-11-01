package dev.findfirst.core.model.jdbc;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("tag")
public class TagJDBC {
  @Id
  private Long id;

  @JsonIgnore
  private int tenantId;

  @JsonIgnore
  private Date createdDate = new Date();

  @JsonIgnore
  private String createdBy = "system";

  @JsonIgnore
  private String lastModifiedBy = "system";

  @JsonIgnore
  private Date lastModifiedDate = new Date();

  @Column("tag_title")
  private String title;

  @Transient
  private Set<BookmarkTag> bookmarks = new HashSet<>();
}
