package dev.findfirst.bookmarkit.service;

import dev.findfirst.bookmarkit.annotations.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.EnabledIf;

// import org.junit.jupiter.api.Test;
@IntegrationTestConfig
@EnabledIf(
    value = "#{{'test', 'prod'}.contains(environment.getActiveProfiles()[0])}",
    loadContext = true)
public class TagServiceTest {
  @Autowired private TagService tagService;

  @Test
  void getNumberOfBookmarksThatBelongToEachTag() {
    // TODO: test our results
    tagService.getTagsWithCnt();
  }
}
