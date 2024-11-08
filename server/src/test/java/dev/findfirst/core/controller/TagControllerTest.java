package dev.findfirst.core.controller;

import static dev.findfirst.utilities.HttpUtility.getHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.dto.BookmarkDTO;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.BookmarkTagPair;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class TagControllerTest {

  @Autowired
  TagControllerTest(TestRestTemplate tRestTemplate) {
    this.restTemplate = tRestTemplate;
  }

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");


  final TestRestTemplate restTemplate;

  @Test
  void addTags() {
    var count = getAllTags().length;
    var tagsToAddList = List.of("hobbies", "sports");
    var tagResp = restTemplate.exchange("/api/tags", HttpMethod.POST,
        getHttpEntity(restTemplate, tagsToAddList), TagDTO[].class);
    var tagOpt = Optional.ofNullable(tagResp.getBody());

    // Test that all the tags added returned
    assertEquals(tagsToAddList.size(), tagOpt.orElseThrow().length);

    // Test that all the tags are returning with the newly added tags
    int expectedCont = count + tagsToAddList.size();
    assertEquals(expectedCont, getAllTags().length);
  }

  @Test
  void deleteAllTags() {

    var bkmksOpts = Optional.ofNullable(restTemplate.exchange("/api/bookmarks", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO[].class).getBody());
    var bkmks = bkmksOpts.orElseThrow();
    assertTrue(bkmks.length > 0, "There should be bookmarks already for this test...");

    var tagCount = getAllTags().length;
    log.debug("\n\n\nChecking current Tags\n");
    log.debug("\n\n\n " + tagCount + "\n");
    for (var t : getAllTags()) {
      log.debug(t.toString());
    }
    var tagResp = Optional.ofNullable(restTemplate
        .exchange("/api/tags", HttpMethod.DELETE, getHttpEntity(restTemplate), TagDTO[].class)
        .getBody());

    var deletedTags = tagResp.orElseThrow();
    for (var del : deletedTags) {
      log.debug(del.toString());
    }
    // Check that all the tags are deleted
    assertEquals(tagCount, deletedTags.length);
    assertEquals(0, getAllTags().length);

    // Get all the bookmarks assert that there are no tags
    bkmksOpts = Optional.ofNullable(restTemplate.exchange("/api/bookmarks", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO[].class).getBody());
    bkmks = bkmksOpts.orElseThrow();
    assertTrue(bkmks.length > 0, "Bookmarks should remain only tags should be deleted.");
    assertTrue(Arrays.stream(bkmks).allMatch(b -> b.tags().size() == 0));
  }

  @Test
  void getTagsByBookmarkId() {
    long tagId = addTag("Testing").id();

    restTemplate.exchange("/api/bookmark/{bookmarkID}/tagId?tagId={id}", HttpMethod.POST,
        getHttpEntity(restTemplate), BookmarkTagPair.class, 1, tagId);

    // See that one tag remains on the bookmark
    var bkmk = Optional.ofNullable(restTemplate.exchange("/api/bookmark?id={id}", HttpMethod.GET,
        getHttpEntity(restTemplate), BookmarkDTO.class, 1).getBody()).orElseThrow();

    assertTrue(bkmk.tags().stream().anyMatch(t -> t.title().equals("Testing")));

    var tagsOpt = Optional.ofNullable(restTemplate.exchange("/api/tag/bkmk?bookmarkId={id}",
        HttpMethod.GET, getHttpEntity(restTemplate), TagDTO[].class, 1).getBody());
    var tags = tagsOpt.orElseThrow();
    assertTrue(tags.length > 0);
  }

  TagDTO addTag(String title) {
    var tagOpts = Optional.ofNullable(restTemplate.exchange("/api/tag?tag={title}", HttpMethod.POST,
        getHttpEntity(restTemplate), TagDTO.class, title).getBody());
    return tagOpts.orElseThrow();
  }

  TagDTO[] getAllTags() {
    var tagResp = restTemplate.exchange("/api/tags", HttpMethod.GET, getHttpEntity(restTemplate),
        TagDTO[].class);
    var tagOpt = Optional.ofNullable(tagResp.getBody());
    var tags = tagOpt.orElseThrow();
    return tags;
  }
}
