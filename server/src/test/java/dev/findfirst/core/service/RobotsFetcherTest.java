package dev.findfirst.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;

import dev.findfirst.core.service.RobotsFetcher.RobotsTxtResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class RobotsFetcherTest {

  @Test
  void testGettingRobotsTxt() {
    RestTemplate restMock = Mockito.mock(RestTemplate.class);
    byte[] expectedContent = "User-agent: *\nDisallow: /admin".getBytes();
    String domain = "https://findfirst.com/robots.txt";

    var expected = new RobotsTxtResponse(200, expectedContent, MediaType.TEXT_PLAIN.toString());
    RobotsFetcher instance = new RobotsFetcher(restMock);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.TEXT_PLAIN);
    ResponseEntity<String> mockReturn =
        new ResponseEntity<String>(new String(expectedContent), headers, HttpStatus.OK);

    when(restMock.getForEntity(any(URI.class), eq(String.class))).thenReturn(mockReturn);
    RobotsTxtResponse rTxtResponse = instance.getRobotsTxt(domain);
    assertEquals(expected, rTxtResponse);
  }
}
