package dev.findfirst.core.controller;

import static dev.findfirst.core.controller.HttpUtility.getHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.repository.TagRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpMethod;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IntegrationTest
public class TagControllerTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @Autowired TagRepository tagRepo;

  @Autowired TestRestTemplate restTemplate;

  @Test
  void getAllTags() {
    var tagResp =
        restTemplate.exchange(
            "/api/tags", HttpMethod.GET, getHttpEntity(restTemplate), Tag[].class);
    var tagOpt = Optional.ofNullable(tagResp.getBody());
    var tags = tagOpt.orElseThrow();
    assertEquals(6, tags.length);
  }
}
