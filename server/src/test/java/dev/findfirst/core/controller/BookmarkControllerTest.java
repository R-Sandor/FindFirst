package dev.findfirst.core.controller;

import static dev.findfirst.utilities.HttpUtility.getHttpEntity;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.dto.AddBkmkReq;
import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.jdbc.BookmarkTag;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;
import dev.findfirst.security.jwt.UserAuthenticationToken;
import dev.findfirst.security.userauth.models.TokenRefreshResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class BookmarkControllerTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  @Autowired
  BookmarkControllerTest(BookmarkJDBCRepository bookmarkJDBCRepository,
      TestRestTemplate tRestTemplate, WebApplicationContext wContext) {
    this.bookmarkJDBCRepository = bookmarkJDBCRepository;
    this.restTemplate = tRestTemplate;
    this.wac = wContext;
  }

  final BookmarkJDBCRepository bookmarkJDBCRepository;
  final TestRestTemplate restTemplate;
  final WebApplicationContext wac;

  private WebTestClient client;

  @BeforeEach
  void setUp() {
    client = MockMvcWebTestClient.bindToApplicationContext(this.wac).build();
  }

  private String baseUrl = "/api/bookmarks";

  @Test
  void containerStarupTest() {
    assertEquals(true, postgres.isRunning());
  }

  @Test
  void shouldFailBecauseNoOneIsAuthenticated() {
    var response = restTemplate.getForEntity(baseUrl, BookmarkDTO.class);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void shouldPassIsAuthenticated() {
    var response = restTemplate.exchange(baseUrl, HttpMethod.GET, getHttpEntity(restTemplate),
        BookmarkDTO[].class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void getBookmarkById() {
    var response = restTemplate.exchange("/api/bookmark?id=1", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    var bkmkOpt = Optional.ofNullable(response.getBody());
    var bkmk = bkmkOpt.orElseThrow();
    assertEquals("Best Cheesecake Recipe", bkmk.title());
  }

  @Test
  void addBookmark() {
    // Test with Scrapping (default behavior)
    var ent = getHttpEntity(restTemplate,
        new AddBkmkReq("Stack Overflow", "https://stackoverflow.com", List.of(), true));
    var response = restTemplate.exchange("/api/bookmark", HttpMethod.POST, ent, BookmarkDTO.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    var bkmk = Optional.ofNullable(response.getBody());
    assertEquals("Stack Overflow - Where Developers Learn, Share, & Build Careers",
        bkmk.orElseThrow().title());

    // Test with scraping (but not allowed as per robot.txt)
    var entFailScrape = getHttpEntity(restTemplate,
        new AddBkmkReq("Facebook", "https://facebook.com", List.of(), true));
    var responseFailScrape = restTemplate.exchange("/api/bookmark", HttpMethod.POST, entFailScrape, BookmarkDTO.class);
    assertEquals(HttpStatus.OK, responseFailScrape.getStatusCode());
    var bkmkFailScrape = Optional.ofNullable(responseFailScrape.getBody());
    assertEquals("", bkmkFailScrape.orElseThrow().title());

    // Test without scrapping
    var entNoScrape = getHttpEntity(restTemplate,
        new AddBkmkReq("Wikipedia", "https://wikipedia.org", List.of(), false));
    var noScrapeResponse = restTemplate.exchange("/api/bookmark", HttpMethod.POST, entNoScrape, BookmarkDTO.class);
    assertEquals(HttpStatus.OK, noScrapeResponse.getStatusCode());

    var noScrapeBkmk = Optional.ofNullable(noScrapeResponse.getBody());
    assertEquals("", noScrapeBkmk.orElseThrow().screenshotUrl());
  }

  @Test
  void addAllBookmarks() {
    saveBookmarks(new AddBkmkReq("", "https://example.com", List.of(), true),
        new AddBkmkReq("goolgleExample", "https://google.com", List.of(), true));
  }

  //
  // @Test
  // void exportAllUsersBookmarks() {
  //   var response = restTemplate.exchange("/api/bookmarks/export", HttpMethod.GET,
  //       getHttpEntity(restTemplate), byte[].class);
  //   String docStr = new String(response.getBody(), StandardCharsets.UTF_8);
  //   assertNotNull(docStr);
  // }

  @Test
  // should test that all of JSmith's bookmarks are deleted but no one else's
  // were removed.
  void deleteAllBookmarks() {
    var response = restTemplate.exchange(baseUrl, HttpMethod.DELETE, getHttpEntity(restTemplate),
        String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(bookmarkJDBCRepository.count() > 0);

    var bkmks = restTemplate.exchange(baseUrl, HttpMethod.GET, getHttpEntity(restTemplate),
        BookmarkDTO[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    var bkmkOpt = Optional.ofNullable(bkmks.getBody());

    assertEquals(0, bkmkOpt.orElseThrow().length);
  }

  @Test
  void deleteTagFromBookmarkByTagTitle() {
    var bkmkResp = saveBookmarks(new AddBkmkReq("yahoo", "https://yahoo.com", List.of(), true));
    var bkmk = bkmkResp.get(0);

    // Add web_dev to bookmark
    var ent = getHttpEntity(restTemplate);
    restTemplate.exchange("/api/bookmark/{bookmarkID}/tag?tag={title}", HttpMethod.POST, ent,
        TagDTO.class, bkmk.id(), "web_dev");

    System.out.println("FOUND TAG");

    // Add design tag to bookmark.
    ent = getHttpEntity(restTemplate);
    restTemplate.exchange("/api/bookmark/{bookmarkID}/tag?tag={title}", HttpMethod.POST, ent,
        TagDTO.class, bkmk.id(), "design");

    var response = restTemplate.exchange("/api/bookmark?id={id}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO.class, bkmk.id());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // Delete first tag.
    restTemplate.exchange("/api/bookmark/{bookmarkID}/tag?tag={tagName}", HttpMethod.DELETE,
        getHttpEntity(restTemplate), TagDTO.class, bkmk.id(), "web_dev");

    response = restTemplate.exchange("/api/bookmark?id={id}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO.class, bkmk.id());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    var bkmkOpt = Optional.ofNullable(response.getBody());
    var bkmkDto = bkmkOpt.orElseThrow();
    assertEquals(1, bkmkDto.tags().size());
    assertFalse(bkmk.tags().stream().anyMatch(t -> t.title().equals("web_dev")));
  }

  @Test
  void deleteTagFromBookmarkById() {
    var bkmkResp = saveBookmarks(
        new AddBkmkReq("Color Picker2", "https://htmlcolorcodes2.com", List.of(), true));
    var bkmk = bkmkResp.get(0);

    // Add Tag web_dev
    var ent = getHttpEntity(restTemplate);
    restTemplate.exchange("/api/bookmark/{bookmarkID}/tag?tag={title}", HttpMethod.POST, ent,
        TagDTO.class, bkmk.id(), "web_dev");

    // Add Tag design
    // Store tag response to delete the tag next
    ent = getHttpEntity(restTemplate);
    var tagOpt = Optional.ofNullable(restTemplate.exchange("/api/bookmark/{bookmarkID}/tag?tag={title}",
        HttpMethod.POST, ent, TagDTO.class, bkmk.id(), "design").getBody());
    long tagId = tagOpt.orElseThrow().id();

    // Delete by the id.
    restTemplate.exchange("/api/bookmark/{bookmarkID}/tagId?tagId={id}", HttpMethod.DELETE,
        getHttpEntity(restTemplate), BookmarkTag.class, bkmk.id(), tagId);

    // See that one tag remains on the bookmark
    var response = restTemplate.exchange("/api/bookmark?id={id}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO.class, bkmk.id());
    assertEquals(HttpStatus.OK, response.getStatusCode());

    var bkmkOptJDBC = Optional.ofNullable(response.getBody());
    var bkmkDto = bkmkOptJDBC.orElseThrow();
    assertFalse(bkmkDto.tags().stream().anyMatch(t -> t.id() == tagId));
  }

  @Test
  void addTagToBookmarkById() {
    var bkmk = saveBookmarks(new AddBkmkReq("duckduckgo", "https://duckduckgo.com", List.of(1L), true));

    var addReq = restTemplate.exchange("/api/bookmark/{bookmarkID}/tagId?tagId={id}",
        HttpMethod.POST, getHttpEntity(restTemplate), BookmarkDTO.class, bkmk.get(0).id(), 5);

    var tags = addReq.getBody().tags();
    log.debug("Printing tags");
    tags.stream().forEach(t -> t.toString());

    assertEquals(2, tags.size());
    assertEquals(2, tags.stream().filter(t -> t.id() == 5 || t.id() == 1).count());
  }

  @Test
  void deleteBookmarkById() {
    saveBookmarks(
        new AddBkmkReq("web scraping", "https://www.scrapethissite.com", List.of(1L, 6L), true));
    var response = restTemplate.exchange(baseUrl, HttpMethod.GET, getHttpEntity(restTemplate),
        BookmarkDTO[].class);
    var bkmkOpt = Optional.ofNullable(response.getBody());

    var id = bkmkOpt.orElseThrow()[0].id();
    var delResp = restTemplate.exchange("/api/bookmark?id={id}", HttpMethod.DELETE,
        getHttpEntity(restTemplate), String.class, id);
    assertEquals(HttpStatus.OK, delResp.getStatusCode());
  }

  /**
   * Test the Flux endpoint for importing bookmarks.
   *
   * @throws IOException
   * @throws InterruptedException
   */
  @Test
  void importBookmarks() throws IOException {
    assertNotNull(new File("google_bookmarks_1_21_24.html"));
    var bodyBuilder = new MultipartBodyBuilder();
    byte[] fileContent = new ClassPathResource("google_bookmarks_1_21_24.html").getInputStream().readAllBytes();

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication authentication = Mockito.mock(Authentication.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    Mockito.when(securityContext.getAuthentication())
        .thenReturn(new UserAuthenticationToken(authentication, 0, null, 1));

    bodyBuilder.part("file", fileContent).filename("BookmarksExample.html");
    HttpHeaders headers = new HttpHeaders();
    // test user
    headers.setBasicAuth("jsmith", "test");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    var signResp = restTemplate.postForEntity("/user/signin", entity, TokenRefreshResponse.class);

    // Get the cookie from signin.
    var cookieOpt = Optional.ofNullable(signResp.getHeaders().get("Set-Cookie"));
    var cookie = cookieOpt.orElseThrow().get(0);
    client.post().uri("/api/bookmark/import").accept(MediaType.APPLICATION_NDJSON)
        .cookie("findfirst", cookie).bodyValue(bodyBuilder.build()).exchange().expectStatus().isOk()
        .expectBodyList(BookmarkDTO.class).hasSize(3);
  }

  @Test
  void jdbcRepo() {
    long count = bookmarkJDBCRepository.count();
    assertTrue(count > 0);
  }

  @Test
  void triggerBookmarkAlreadyExistException() {
    saveBookmarks(new AddBkmkReq("search", "https://bing.com", new ArrayList<>(), true));
    var ent = getHttpEntity(restTemplate, new AddBkmkReq("search", "https://bing.com", new ArrayList<>(), true));
    var blResp = restTemplate.exchange("/api/bookmark", HttpMethod.POST, ent,
        BookmarkDTO[].class);
    assertEquals(HttpStatus.CONFLICT, blResp.getStatusCode());
  }

  private List<BookmarkDTO> saveBookmarks(AddBkmkReq... newBkmks) {
    HttpEntity<?> ent;
    // Test can not handle covariant return type of [] vs a single Bookmark.
    if (newBkmks.length == 1) {
      ent = getHttpEntity(restTemplate, newBkmks[0]);
      var bkmkResp = restTemplate.exchange("/api/bookmark", HttpMethod.POST, ent, BookmarkDTO.class);
      return List.of(bkmkResp.getBody());
    }
    ent = getHttpEntity(restTemplate, Arrays.asList(newBkmks));
    var blResp = restTemplate.exchange("/api/bookmark/addBookmarks", HttpMethod.POST, ent,
        BookmarkDTO[].class);
    assertEquals(HttpStatus.OK, blResp.getStatusCode());
    return List.of(blResp.getBody());
  }
}
