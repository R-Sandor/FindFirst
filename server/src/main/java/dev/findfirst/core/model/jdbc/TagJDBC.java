package dev.findfirst.core.model.jdbc;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@Table("tag")
public class TagJDBC {
  @Id
  private Long id;

  @JsonIgnore
  private Integer tenantId;

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
}
