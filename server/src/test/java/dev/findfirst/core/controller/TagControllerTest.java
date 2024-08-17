package dev.findfirst.core.controller;

import static dev.findfirst.utilities.HttpUtility.getHttpEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
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

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.BookmarkTagPair;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.repository.TagRepository;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@IntegrationTest
public class TagControllerTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  @Autowired
  TagRepository tagRepo;

  @Autowired
  TestRestTemplate restTemplate;

  @Test
  void addTags() {
    var count = getAllTags().length;
    var tagsToAddList = List.of("hobbies", "sports");
    var tagResp = restTemplate.exchange("/api/tags", HttpMethod.POST,
        getHttpEntity(restTemplate, tagsToAddList), Tag[].class);
    var tagOpt = Optional.ofNullable(tagResp.getBody());

    // Test that all the tags added returned
    assertEquals(tagsToAddList.size(), tagOpt.orElseThrow().length);

    // Test that all the tags are returning with the newly added tags
    int expectedCont = count + tagsToAddList.size();
    assertEquals(expectedCont, getAllTags().length);
  }

  @Test
  void deleteAllTags() {

    var bkmksOpts = Optional.ofNullable(restTemplate
        .exchange("/api/bookmarks", HttpMethod.GET, getHttpEntity(restTemplate), Bookmark[].class)
        .getBody());
    var bkmks = bkmksOpts.orElseThrow();
    assertTrue(bkmks.length > 0, "There should be bookmarks already for this test...");

    var tagCount = getAllTags().length;
    var tagResp = Optional.ofNullable(restTemplate
        .exchange("/api/tags", HttpMethod.DELETE, getHttpEntity(restTemplate), Tag[].class)
        .getBody());

    var deletedTag = tagResp.orElseThrow();
    // Check that all the tags are deleted
    assertTrue(deletedTag.length == tagCount);
    assertTrue(getAllTags().length == 0);

    // Get all the bookmarks assert that there are no tags
    bkmksOpts = Optional.ofNullable(restTemplate
        .exchange("/api/bookmarks", HttpMethod.GET, getHttpEntity(restTemplate), Bookmark[].class)
        .getBody());
    bkmks = bkmksOpts.orElseThrow();
    assertTrue(bkmks.length > 0, "Bookmarks should remain only tags should be deleted.");
    assertTrue(Arrays.stream(bkmks).allMatch(b -> b.getTags().size() == 0));
  }

  @Test
  void getTagsByBookmarkId() {
    long tagId = addTag("Testing").getId();

    restTemplate.exchange("/api/bookmark/{bookmarkID}/tagId?tagId={id}", HttpMethod.POST,
        getHttpEntity(restTemplate), BookmarkTagPair.class, 1, tagId);

    // See that one tag remains on the bookmark
    var bkmk = Optional.ofNullable(restTemplate.exchange("/api/bookmark?id={id}", HttpMethod.GET,
        getHttpEntity(restTemplate), Bookmark.class, 1).getBody()).orElseThrow();

    assertTrue(bkmk.getTags().stream().anyMatch(t -> t.getTag_title().equals("Testing")));

    var tagsOpt = Optional.ofNullable(restTemplate.exchange("/api/tag/bkmk?bookmarkId={id}",
        HttpMethod.GET, getHttpEntity(restTemplate), Tag[].class, 1).getBody());
    var tags = tagsOpt.orElseThrow();
    assertTrue(tags.length > 0);
  }

  Tag addTag(String title) {
    var tagOpts = Optional.ofNullable(restTemplate.exchange("/api/tag?tag={title}", HttpMethod.POST,
        getHttpEntity(restTemplate), Tag.class, title).getBody());
    return tagOpts.orElseThrow();
  }

  Tag[] getAllTags() {
    var tagResp = restTemplate.exchange("/api/tags", HttpMethod.GET, getHttpEntity(restTemplate),
        Tag[].class);
    var tagOpt = Optional.ofNullable(tagResp.getBody());
    var tags = tagOpt.orElseThrow();
    return tags;
  }
}
