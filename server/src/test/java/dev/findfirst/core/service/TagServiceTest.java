package dev.findfirst.core.service;

import dev.findfirst.core.annotations.IntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTestConfig
public class TagServiceTest {
  @Autowired private TagService tagService;

  @Test
  public void getNumberOfBookmarksThatBelongToEachTag() {
    tagService.getTagsWithCnt();
  }
}
