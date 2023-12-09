package dev.findfirst.security.userAuth.tenant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

  @CreatedBy
  @Column(updatable = false)
  @JsonIgnore
  private String createdBy;

  @CreatedDate
  @Column(updatable = false)
  @JsonIgnore
  private Date createdDate;

  @LastModifiedBy @JsonIgnore private String lastModifiedBy;

  @LastModifiedDate @JsonIgnore private Date lastModifiedDate;
}
