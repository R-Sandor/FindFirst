package dev.findfirst.bookmarkit.security.listeners;

import dev.findfirst.bookmarkit.security.contexts.TenantContext;
import dev.findfirst.bookmarkit.security.model.Tenantable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;

public class TenantEntityListener {

  @Autowired private TenantContext tenantContext;

  @PrePersist
  @PreUpdate
  public void prePersistAndUpdate(Object object) {
    if (object instanceof Tenantable) {
      ((Tenantable) object).setTenantId(tenantContext.getTenantId());
    }
  }

  @PreRemove
  public void preRemove(Object object) {
    if (object instanceof Tenantable
        && ((Tenantable) object).getTenantId() != tenantContext.getTenantId()) {
      throw new EntityNotFoundException();
    }
  }
}
