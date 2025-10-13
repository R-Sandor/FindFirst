package dev.findfirst.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class SearchBkmkByTagReqTest {

  @Test
  void tagRequestsEqual() {
    var tags = new SearchBkmkByTagReq(List.of("tech", "docs"));
    assertEquals(new SearchBkmkByTagReq(List.of("tech", "docs")),
        new SearchBkmkByTagReq(List.of("tech", "docs")));
    assertNotEquals(new SearchBkmkByTagReq(List.of("tech", "docs")),
        new SearchBkmkByTagReq(List.of("tech")));
    assertFalse(tags.equals(null));
    assertFalse(tags.equals((Object) "Strings"));
  }
}
