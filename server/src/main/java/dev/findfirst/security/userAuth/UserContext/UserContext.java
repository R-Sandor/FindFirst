package dev.findfirst.security.userAuth.UserContext;

import dev.findfirst.security.jwt.UserAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

  public int getUserId() {
    return ((UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
        .getUserId();
  }
}
