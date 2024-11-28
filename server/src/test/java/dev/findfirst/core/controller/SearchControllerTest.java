package dev.findfirst.core.controller;

import static dev.findfirst.utilities.HttpUtility.getHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.model.SearchBkmkByTagReq;
import dev.findfirst.core.model.SearchBkmkByTextReq;
import dev.findfirst.core.model.SearchBkmkByTitleReq;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SearchControllerTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  final TestRestTemplate restTemplate;

  @Test
  void searchByTags() {
    var resp = restTemplate.exchange("/api/search/tags?tag={}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO[].class, new SearchBkmkByTagReq("Test"));
    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void searchByTitle() {
    var resp = restTemplate.exchange("/api/search/title?title={}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO[].class, new SearchBkmkByTitleReq(new String[]{"Test", "Title"}));

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void searchByText() {
    var resp =
        restTemplate.exchange("/api/search/text?text={}", HttpMethod.GET, getHttpEntity(restTemplate),
            BookmarkDTO[].class, new SearchBkmkByTextReq("Text in bookmark"));

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }
}
