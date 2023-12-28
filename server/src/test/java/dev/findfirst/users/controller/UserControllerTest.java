package dev.findfirst.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.security.userAuth.models.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

  @Autowired TestRestTemplate restTemplate;

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  private String userUrl = "/user";

  @Test
  void userSignup() {
    var headers = new HttpHeaders();
    var ent =
        new HttpEntity<>(
            new SignupRequest(
                "Stephen Hayes", "Steve@gmail.com", "Steve-Man", "$tev3s_sup3rH@rdPassword"),
            headers);
    var response = restTemplate.exchange(userUrl + "/signup", HttpMethod.POST, ent, Bookmark.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
