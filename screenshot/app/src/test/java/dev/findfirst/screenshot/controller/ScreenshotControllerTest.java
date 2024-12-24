package dev.findfirst.screenshot.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ScreenshotControllerTest {

  final TestRestTemplate restTemplate;

  @Test
  void imageRequest() {
    Assertions.assertNotNull(restTemplate);
    var resp = restTemplate.getForObject("/screenshot/url?{}" ,  byte[].class, "https://google.com");
    Assertions.assertTrue(resp.length > 0);
  }
}
