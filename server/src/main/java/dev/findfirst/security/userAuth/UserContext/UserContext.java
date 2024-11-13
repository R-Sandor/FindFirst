package dev.findfirst.security.userAuth.UserContext;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.findfirst.security.jwt.UserAuthenticationToken;

@Component
public class UserContext {

  public int getUserId() {
    return ((UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
        .getUserId();
  }
}
