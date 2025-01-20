package dev.findfirst.core.controller;

import static dev.findfirst.utilities.HttpUtility.getHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.annotations.MockTypesense;
import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.dto.BookmarkOnly;
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
@MockTypesense
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SearchControllerTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  final TestRestTemplate restTemplate;

  @Test
  void searchByTags() {
    var resp = restTemplate.exchange("/api/search/tags?tags={tag1},{tag2}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkOnly[].class, "Cooking", "web_dev");
    assertEquals(HttpStatus.OK, resp.getStatusCode());
    var bkmks = resp.getBody();
    assertEquals(3, bkmks.length);
    var titles = new ArrayList<String>();
    for (int i = 0; i < 3; i++) {
      titles.add(bkmks[i].title());
    }
    assertTrue(titles.contains("Best Cheesecake Recipe"));
  }

  @Test
  void searchByTitle() {
    var resp = restTemplate.exchange("/api/search/title?keywords={}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO[].class,
        new SearchBkmkByTitleReq(new String[] {"Test", "Title"}));

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void searchByText() {
    var resp = restTemplate.exchange("/api/search/text?text={}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO[].class,
        new SearchBkmkByTextReq("Text in bookmark"));

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }
}
