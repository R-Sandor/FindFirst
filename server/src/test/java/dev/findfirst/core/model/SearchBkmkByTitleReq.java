package dev.findfirst.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class SearchBkmkByTitleReqTest {

  @Test
  void titleRequestsEquality() {
    assertEquals(new SearchBkmkByTitleReq(new String[] { "java", "spring" }),
        new SearchBkmkByTitleReq(new String[] { "java", "spring" }));
    assertNotEquals(new SearchBkmkByTitleReq(new String[] { "java" }),
        new SearchBkmkByTitleReq(new String[] { "java", "spring" }));
    assertFalse(new SearchBkmkByTitleReq(new String[] { "java", "spring" }).equals((Object) "String"));
    assertFalse(new SearchBkmkByTitleReq(new String[] { "java", "spring" }).equals(null));
  }

  @Test
  void hashCodeTest() {
    var keywords = new String[] { "java", "spring" };
    var searchReq = new SearchBkmkByTitleReq(keywords);
    assertEquals(Arrays.hashCode(keywords), searchReq.hashCode());
  }

  @Test
  void toStringTest() {
    var keywords = new String[] { "java", "spring" };
    var searchReq = new SearchBkmkByTitleReq(keywords);
    assertEquals(Arrays.toString(keywords), searchReq.toString());

  }
}
