package dev.findfirst.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class SearchBkmkByTagReqTest {

  @Test
  void tagRequestsEqual() {
    assertEquals(new SearchBkmkByTagReq(List.of("tech", "docs")),
        new SearchBkmkByTagReq(List.of("tech", "docs")));
    assertNotEquals(new SearchBkmkByTagReq(List.of("tech", "docs")),
        new SearchBkmkByTagReq(List.of("tech")));
    assertNotEquals("String", new SearchBkmkByTagReq(List.of("tech", "docs")));
  }
}
