package dev.findfirst.security.userAuth.tenant.contexts;

import dev.findfirst.security.jwt.TenantAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TenantContext {

  public int getTenantId() {
    return ((TenantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
        .getTenantId();
  }
}
