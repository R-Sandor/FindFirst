package dev.findfirst.core.controller;

import static dev.findfirst.utilities.HttpUtility.getHttpEntity;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.annotations.MockTypesense;

import lombok.RequiredArgsConstructor;
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

@Testcontainers
@IntegrationTest
@MockTypesense
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ImageControllerTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  final TestRestTemplate restTemplate;

  @Test
  void imageRequest() {
    String filename = "facebook.com.png";
    Assertions.assertNotNull(restTemplate);
    var resp = restTemplate.exchange("/api/screenshots/" + filename, HttpMethod.GET,
        getHttpEntity(restTemplate), byte[].class);
    Assertions.assertTrue(resp.getBody().length > 0);


    resp = restTemplate.exchange("/api/screenshots/" + filename + "fake", HttpMethod.GET,
        getHttpEntity(restTemplate), byte[].class);
    Assertions.assertNull(resp.getBody());
  }

}
