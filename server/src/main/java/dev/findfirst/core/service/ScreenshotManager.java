package dev.findfirst.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScreenshotManager {

  @Value("${screenshot.service.url:http://localhost:8080}") private String screenshotServiceUrl;

  @Autowired
  private RestTemplate rest;

  public String getScreenshot(String reqUrl) {
    String url = screenshotServiceUrl + "/screenshot?url=" + reqUrl;
    return rest.getForObject(url, String.class);
  }

}
