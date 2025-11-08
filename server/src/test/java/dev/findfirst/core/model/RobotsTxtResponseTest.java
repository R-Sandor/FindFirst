package dev.findfirst.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import dev.findfirst.core.service.RobotsFetcher;
import dev.findfirst.core.service.RobotsFetcher.RobotsTxtResponse;

import org.junit.jupiter.api.Test;

class RobotsTxtResponseTest {
  RobotsTxtResponse response200 = new RobotsFetcher.RobotsTxtResponse(200, "response from server".getBytes(), "json");
  RobotsTxtResponse response200Same = new RobotsFetcher.RobotsTxtResponse(200, "response from server".getBytes(),
      "json");
  RobotsTxtResponse response400 = new RobotsFetcher.RobotsTxtResponse(400, "response from server".getBytes(), "json");
  RobotsTxtResponse responseContentJson = new RobotsFetcher.RobotsTxtResponse(200, "new message".getBytes(), "json");
  RobotsTxtResponse responseContentHtml = new RobotsFetcher.RobotsTxtResponse(400, "response from server".getBytes(),
      "html");
  RobotsTxtResponse responseContent2 = new RobotsFetcher.RobotsTxtResponse(400, "response from server".getBytes(),
      null);

  @Test
  void robotsTxtResponseEquality() {
    assertEquals(response200, response200Same);
    assertNotEquals(response200, responseContentJson);
    assertNotEquals(response200, response400);
    assertNotEquals(response200, (Object) null);
    assertNotEquals(response200, (Object) "");
    assertNotEquals(response200, responseContentHtml);
    assertNotEquals(responseContentHtml, responseContent2);
  }

  @Test
  void hashingTests() {
    assertEquals(response200.hashCode(), response200.hashCode());
    assertEquals(response200Same.hashCode(), response200.hashCode());
    assertNotEquals(response200.hashCode(), responseContentJson.hashCode());
    assertNotEquals(response200.hashCode(), response400.hashCode());
    assertNotEquals(response200.hashCode(), "".hashCode());
    assertNotEquals(response200.hashCode(), responseContentHtml.hashCode());
    assertNotEquals(responseContentHtml.hashCode(), responseContent2.hashCode());
  }

  @Test
  void toStringTest() {
    assertEquals(response200.toString(), response200.toString());
    assertEquals(response200Same.toString(), response200.toString());
    assertNotEquals(response200.toString(), responseContentJson.toString());
    assertNotEquals(response200.toString(), response400.toString());
    assertNotEquals(response200.toString(), (Object) "");
    assertNotEquals(response200.toString(), responseContentHtml.toString());
    assertNotEquals(responseContentHtml.toString(), responseContent2.toString());
  }

}
