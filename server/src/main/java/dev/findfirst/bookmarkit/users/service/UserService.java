package dev.findfirst.bookmarkit.users.service;

import dev.findfirst.bookmarkit.users.model.User;
import dev.findfirst.bookmarkit.users.repository.UserRepo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  @Autowired UserRepo userRepo;

  public Optional<User> getUserByEmail(String email) {
    return userRepo.findByEmail(email);
  }

  public Optional<User> getUserByUsername(String username) {
    return userRepo.findByUsername(username);
  }
}
