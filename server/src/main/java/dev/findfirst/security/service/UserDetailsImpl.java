package dev.findfirst.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.findfirst.core.users.model.user.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;

  private String email;

  @JsonIgnore private String password;

  private GrantedAuthority authority;

  public UserDetailsImpl(
      Long id, String username, String email, String password, GrantedAuthority authority) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authority = authority;
  }

  public static UserDetailsImpl build(User user) {
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().name());

    return new UserDetailsImpl(
        user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), authority);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(authority);
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
