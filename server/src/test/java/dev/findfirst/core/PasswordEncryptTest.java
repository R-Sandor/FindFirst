package dev.findfirst.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptTest {

  @Test
  public void encodeEncryptUserPassword() {
    String password = "test";
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String testPasswordEncoded = passwordEncoder.encode(password);
    System.out.println("encoded password = " + testPasswordEncoded);
    Assertions.assertNotEquals(passwordEncoder.encode(password + " "), testPasswordEncoded);
  }
}
