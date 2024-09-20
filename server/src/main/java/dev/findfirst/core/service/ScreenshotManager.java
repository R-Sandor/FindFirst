package dev.findfirst.core.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ScreenshotManager {

  @Value("${screenshot.service.url}") private String screenshotServiceUrl;

  private final RestTemplate rest;

  public Optional<String> getScreenshot(String reqUrl) {
    String url = screenshotServiceUrl + "/screenshot?url=" + reqUrl;
    return Optional.ofNullable(rest.getForObject(url, String.class));
  }

}
