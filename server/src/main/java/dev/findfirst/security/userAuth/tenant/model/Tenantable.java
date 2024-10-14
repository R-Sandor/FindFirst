package dev.findfirst.security.userAuth.tenant.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import dev.findfirst.security.userAuth.tenant.listeners.TenantEntityListener;
import dev.findfirst.security.userAuth.utils.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Data
@MappedSuperclass
@FilterDef(name = Constants.TENANT_FILTER_NAME,
    parameters = @ParamDef(name = Constants.TENANT_PARAMETER_NAME, type = Integer.class),
    defaultCondition = Constants.TENANT_COLUMN_NAME + " = :" + Constants.TENANT_PARAMETER_NAME)
@Filter(name = Constants.TENANT_FILTER_NAME)
@EntityListeners(TenantEntityListener.class)
public class Tenantable extends Auditable {

  @JsonIgnore
  @Column(nullable = false)
  private Integer tenantId;
}
