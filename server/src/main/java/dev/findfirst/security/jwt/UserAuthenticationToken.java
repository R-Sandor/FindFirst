package dev.findfirst.security.jwt;

import java.util.Collection;

import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode
public class UserAuthenticationToken extends AbstractAuthenticationToken {

  private final transient Object principal;
  private final int userId;
  private final int roleId;

  public UserAuthenticationToken(Object principal, int roleId,
      Collection<? extends GrantedAuthority> authorities, int userId) {
    super(authorities);
    setAuthenticated(true);
    this.principal = principal;
    this.roleId = roleId;
    this.userId = userId;
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public int getUserId() {
    return this.userId;
  }

  public int getRoleId() {
    return this.roleId;
  }
}
