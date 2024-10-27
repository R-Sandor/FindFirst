package dev.findfirst.security.userAuth.tenant.listeners;


import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;
import dev.findfirst.security.userAuth.tenant.model.Tenantable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TenantEntityListener {

  private final TenantContext tenantContext;

  @PrePersist
  @PreUpdate
  public void prePersistAndUpdate(Object object) {
    if (!isListenerEnabled.get())
      return;
    if (object instanceof Tenantable) {
      ((Tenantable) object).setTenantId(tenantContext.getTenantId());
    }
  }

  @PreRemove
  public void preRemove(Object object) {
    if (!isListenerEnabled.get())
      return;
    if (object instanceof Tenantable
        && ((Tenantable) object).getTenantId() != tenantContext.getTenantId()) {
      throw new EntityNotFoundException();
    }
  }

  private static final ThreadLocal<Boolean> isListenerEnabled = ThreadLocal.withInitial(() -> true);

  public static void disableListener() {
    isListenerEnabled.set(false);
  }

  public static void enableListener() {
    isListenerEnabled.set(true);
  }
}
