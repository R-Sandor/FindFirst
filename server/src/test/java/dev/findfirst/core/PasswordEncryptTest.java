package dev.findfirst.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PasswordEncryptTest {

  @Test
  public void encodeEncryptUserPassword() {
    String password = "test";
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String testPasswordEncoded = passwordEncoder.encode(password);
    System.out.println("encoded password = " + testPasswordEncoded);
  }
}
