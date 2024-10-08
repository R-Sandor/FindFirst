package dev.findfirst.core.controller;

import static dev.findfirst.utilities.HttpUtility.getHttpEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpMethod;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import dev.findfirst.core.annotations.IntegrationTest;
import lombok.RequiredArgsConstructor;

@Testcontainers
@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ImageControllerTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  // @Autowired
  // ImageControllerTest(TestRestTemplate restTemplate) { 
  //   this.restTemplate = restTemplate;
  // }

  final TestRestTemplate restTemplate;

  @Test
  void imageRequest() {
    var resp = restTemplate.exchange("/api/screenshots", HttpMethod.GET, getHttpEntity(restTemplate), byte[].class);
    Assertions.assertTrue(resp.getBody().length > 0);
  }

}
