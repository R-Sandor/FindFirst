package dev.findfirst.core.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import dev.findfirst.core.service.RobotsFetcher;
import dev.findfirst.core.service.RobotsFetcher.RobotsTxtResponse;

public class RobotsTxtResponseTest {

  @Test
  void robotsTxtResponseEquality() {
    RobotsTxtResponse response1 = new RobotsFetcher.RobotsTxtResponse(200, "response from server".getBytes(), "json");
    RobotsTxtResponse response2 = new RobotsFetcher.RobotsTxtResponse(200, "response from server".getBytes(), "json");
    RobotsTxtResponse response3 = new RobotsFetcher.RobotsTxtResponse(200, "new message".getBytes(), "json");
    RobotsTxtResponse response400 = new RobotsFetcher.RobotsTxtResponse(400, "response from server".getBytes(), "json");
    RobotsTxtResponse responseContent = new RobotsFetcher.RobotsTxtResponse(400, "response from server".getBytes(),
        "html");
    RobotsTxtResponse responseContent2 = new RobotsFetcher.RobotsTxtResponse(400, "response from server".getBytes(),
        null);

    assertTrue(response1.equals(response2));
    assertNotEquals(response1, response3);
    assertNotEquals(response1, response400);
    assertNotEquals(response1, null);
    assertNotEquals(response1, (Object) "");
    assertNotEquals(response1, responseContent);
    assertNotEquals(responseContent, responseContent2);
  }

}
