package dev.renegade.bookmarkit.users.service;

import dev.renegade.bookmarkit.users.model.User;
import dev.renegade.bookmarkit.users.repo.UserRepo;
import java.util.Optional;

import lombok.Data;
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
}
