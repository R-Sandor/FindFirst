package dev.findfirst.core.model;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.findfirst.core.service.RobotsFetcher;
import dev.findfirst.core.service.RobotsFetcher.RobotsTxtResponse;

public class RobotsTxtResponseTest {
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
    assertNotEquals(response200, null);
    assertNotEquals(response200, (Object) "");
    assertNotEquals(response200, responseContentHtml);
    assertNotEquals(responseContentHtml, responseContent2);
  }

  @Test
  void hashingTests() {
    // response1
  }

}
