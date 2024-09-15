package dev.findfirst.security.userAuth.tenant.listeners;

import lombok.RequiredArgsConstructor;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;
import dev.findfirst.security.userAuth.tenant.model.Tenantable;

@RequiredArgsConstructor
public class TenantEntityListener {

  private final TenantContext tenantContext;

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
