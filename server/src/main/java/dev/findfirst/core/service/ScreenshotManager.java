package dev.findfirst.core.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreenshotManager {

  @Value("${screenshot.service.url}")
  private String screenshotServiceUrl;

  private final RestTemplate rest;

  public Optional<String> getScreenshot(String reqUrl) {
    log.debug("Getting screenshot request");
    String url = screenshotServiceUrl + "/screenshot?url=" + reqUrl;
    String screenshotUrl;
    try {
      screenshotUrl = rest.getForObject(url, String.class);
    } catch (ResourceAccessException ex) {
      log.error("Exeception: {}" + ex.getMessage());
      return Optional.ofNullable(null);
    }
    log.debug("Screenshot {}", screenshotUrl);

    return Optional.ofNullable(screenshotUrl);
  }

}
