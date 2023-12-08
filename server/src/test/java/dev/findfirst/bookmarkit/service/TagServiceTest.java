package dev.findfirst.bookmarkit.service;

import dev.findfirst.bookmarkit.annotations.IntegrationTestConfig;
import dev.findfirst.core.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTestConfig
public class TagServiceTest {
  @Autowired private TagService tagService;

  @Test
  void getNumberOfBookmarksThatBelongToEachTag() {
    // TODO: test our results
    tagService.getTagsWithCnt();
  }
}
