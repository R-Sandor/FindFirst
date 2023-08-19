package dev.findfirst.bookmarkit.security.jwt;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class TenantAuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;
  private final int tenantId;
  private final int roleId;

  public TenantAuthenticationToken(
      Object principal,
      int roleId,
      Collection<? extends GrantedAuthority> authorities,
      int tenantId) {
    super(authorities);
    setAuthenticated(true);
    this.principal = principal;
    this.roleId = roleId;
    this.tenantId = tenantId;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public int getTenantId() {
    return this.tenantId;
  }

  public int getRoleId() {
    return this.roleId;
  }
}
