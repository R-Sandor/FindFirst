package dev.findfirst.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ScreenshotManager {

  @Value("${screenshot.service.url}") private String screenshotServiceUrl;

  private final RestTemplate rest;

  public String getScreenshot(String reqUrl) {
    String url = screenshotServiceUrl + "/screenshot?url=" + reqUrl;
    return rest.getForObject(url, String.class);
  }

}
