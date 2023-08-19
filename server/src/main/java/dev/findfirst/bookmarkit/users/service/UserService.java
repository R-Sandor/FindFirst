package dev.findfirst.bookmarkit.users.service;

import dev.findfirst.bookmarkit.security.execeptions.NoUserFoundException;
import dev.findfirst.bookmarkit.users.model.user.User;
import dev.findfirst.bookmarkit.users.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  @Autowired UserRepo userRepo;

  public User getUserByEmail(String email) {
    return userRepo.findByEmail(email).orElseThrow(NoUserFoundException::new);
  }

  public User getUserByUsername(String username) {
    return userRepo.findByUsername(username).orElseThrow(NoUserFoundException::new);
  }
}
