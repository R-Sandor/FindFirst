package dev.findfirst.screenshot.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ScreenshotControllerTest {

  final TestRestTemplate restTemplate;

  @Test
  void imageRequest() {
    Assertions.assertNotNull(restTemplate);
    var resp =
        restTemplate.getForEntity("/screenshot?url={url}", byte[].class, "https://google.com");
    Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode());
    Assertions.assertTrue(resp.getBody().length > 0);
  }

  @Test
  void bogus() {
    Assertions.assertNotNull(restTemplate);
    var resp =
        restTemplate.getForEntity("/screenshot?url={url}", byte[].class, "htts:/bogusgoogle.com");
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }
}
