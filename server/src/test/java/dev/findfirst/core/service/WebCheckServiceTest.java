package dev.findfirst.core.service;


import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WebCheckServiceTest {

  @MockBean
  RobotsFetcher robotsFetcher;

  @Autowired
  WebCheckService webCheckService;


  @Test
  public void isScrapable() {
    String robotsString = """
        User-agent: *
        Allow: /api/v*/store
        Allow: /api/v*/applications
        Allow: /api/v*/application-directory-static
        Allow: /api/v*/invite
        Allow: /api/v*/discovery
        Allow: /api/discovery
        Allow: /invite
        Allow: /invite/
        Allow: /terms
        Allow: /privacy
        Disallow: /channels
        Disallow: /channels/
        Disallow: /verify
        Disallow: /verify/
        """;


    Mockito.when(robotsFetcher.getRobotsTxt(anyString())).thenReturn(
        new RobotsFetcher.RobotsTxtResponse(200, robotsString.getBytes(), "text/plain"));

    Assertions.assertTrue(webCheckService.isScrapable("https://discord.com/api/discovery"));
    Assertions.assertTrue(webCheckService.isScrapable("https://discord.com/invite/test.txt"));
    Assertions.assertTrue(webCheckService.isScrapable("https://discord.com/api/vrevef/invite"));
    Assertions.assertFalse(webCheckService.isScrapable("https://discord.com/channels"));
  }

}
