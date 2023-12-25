package dev.findfirst.core.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.findfirst.core.annotations.IntegrationTestConfig;
import dev.findfirst.core.model.AddBkmkReq;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.repository.BookmarkRepository;
import dev.findfirst.security.userAuth.models.TokenRefreshResponse;
import java.util.List;
import java.util.Optional;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IntegrationTestConfig
public class BookmarkControllerTest {

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @Autowired BookmarkRepository bkmkRepo;

  @Autowired TestRestTemplate restTemplate;
  private static String baseUrl = "/api/bookmarks";

  @Test
  void containerStarupTest() {
    assertEquals(postgres.isRunning(), true);
  }

  @Test
  void shouldFailBecauseNoOneIsAuthenticated() {
    var response = restTemplate.getForEntity(baseUrl, Bookmark.class);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void shouldPassIsAuthenticated() {
    var response =
        restTemplate.exchange(baseUrl, HttpMethod.GET, getHttpEntity(), Bookmark[].class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    var bkmkOpt = Optional.ofNullable(response.getBody());
    assertEquals(3, bkmkOpt.orElseThrow().length);
  }

  @Test
  void getBookmarkById() {
    var response =
        restTemplate.exchange(
            "/api/bookmark?id=1", HttpMethod.GET, getHttpEntity(), Bookmark.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    var bkmkOpt = Optional.ofNullable(response.getBody());
    var bkmk = bkmkOpt.orElseThrow();
    assertEquals("Best Cheesecake Recipe", bkmk.getTitle());
  }

  @Test
  void addBookmark() {
    var ent = getHttpEntity(new AddBkmkReq("Facebook", "facebook.com", List.of()));
    var response = restTemplate.exchange("/api/bookmark", HttpMethod.POST, ent, Bookmark.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    var bkmk = Optional.ofNullable(response.getBody());
    assertEquals("Facebook", bkmk.orElseThrow().getTitle());
  }

  @Test
  // should test that all of JSmith's bookmarks are deleted but no one else's
  // were removed.
  void deleteAllbookmarks() {
    var response =
        restTemplate.exchange(
            baseUrl + "/deleteAll", HttpMethod.POST, getHttpEntity(), String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(bkmkRepo.count() > 0);

    var bkmks = restTemplate.exchange(baseUrl, HttpMethod.GET, getHttpEntity(), Bookmark[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    var bkmkOpt = Optional.ofNullable(bkmks.getBody());

    assertEquals(0, bkmkOpt.orElseThrow().length);
  }

@Test
 void deleteTagFromBookmarkByBookmarkID() {
    var ent = getHttpEntity(new AddBkmkReq("Web Color Picker", "htmlcolorcodes.com", List.of()));
    var addBmkResp = restTemplate.exchange("/api/bookmark", HttpMethod.POST, ent, Bookmark.class);
    assertEquals(HttpStatus.OK, addBmkResp.getStatusCode());
    var bkmkOpt = Optional.ofNullable(addBmkResp.getBody());
    var bkmk = bkmkOpt.orElseThrow();
    
    ent = getHttpEntity(new Tag("web_dev"));
    restTemplate.exchange("/api/bookmark/{BookmarkID}", HttpMethod.POST, ent, Tag.class, bkmk.getId());
    ent = getHttpEntity(new Tag("design"));
    restTemplate.exchange("/api/bookmark/{BookmarkID}", HttpMethod.POST, ent, Tag.class, bkmk.getId());
    restTemplate.exchange("/api/bookmark/{BookmarkID}?tag={tagName}", HttpMethod.DELETE, getHttpEntity(), Tag.class,
      bkmk.getId(), "web_dev"
    );

    var response =
    restTemplate.exchange(
        "/api/bookmark?id={id}", HttpMethod.GET, getHttpEntity(), Bookmark.class, bkmk.getId());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    bkmkOpt = Optional.ofNullable(response.getBody());
    bkmk = bkmkOpt.orElseThrow();
    assertEquals(1, bkmk.getTags().size());
    assertFalse(bkmk.getTags().stream().anyMatch(t -> t.getTag_title().equals("web_dev")));

  }


  private HttpEntity<?> getHttpEntity() {
    HttpHeaders headers = new HttpHeaders();
    // test user
    headers.setBasicAuth("jsmith", "test");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    var signResp = restTemplate.postForEntity("/user/signin", entity, TokenRefreshResponse.class);

    // Get the cookie from signin.
    var cookieOpt = Optional.ofNullable(signResp.getHeaders().get("Set-Cookie"));
    var cookie = cookieOpt.orElseThrow();

    // Add the cookie to next request.
    headers = new HttpHeaders();
    headers.add("Cookie", cookie.get(0));
    return new HttpEntity<>(headers);
  }

  private <T> HttpEntity<?> getHttpEntity(T body) {
    HttpHeaders headers = new HttpHeaders();
    // test user
    headers.setBasicAuth("jsmith", "test");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    var signResp = restTemplate.postForEntity("/user/signin", entity, TokenRefreshResponse.class);

    // Get the cookie from signin.
    var cookieOpt = Optional.ofNullable(signResp.getHeaders().get("Set-Cookie"));
    var cookie = cookieOpt.orElseThrow();

    // Add the cookie to next request.
    headers = new HttpHeaders();
    headers.add("Cookie", cookie.get(0));
    return new HttpEntity<>(body, headers);
  }
}
