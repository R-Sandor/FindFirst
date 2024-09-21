package dev.findfirst.screenshot.controller;

import com.microsoft.playwright.*;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@Slf4j
public class ScreenshotController {

  @Value("${findfirst.screenshot.location}")
  private String screenshotSaveLoc;

  @PostConstruct
  public void init() {
    try (Playwright unused = Playwright.create()) {
      log.info("Browsers downloaded");
    }
  }

  @GetMapping("/screenshot")
  public String takeScreenshot(@RequestParam String url) {
    // Use a try-with-resources block to manage the Playwright resources
    try (Playwright playwright = Playwright.create()) {
      // Use Chromium for this example; you can choose another browser type
      BrowserType browserType = playwright.chromium();
      try (Browser browser = browserType.launch()) {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate(url);
        String cleanUrl = url.replace("/", "_");
        Path filePath = Path.of(screenshotSaveLoc, cleanUrl + System.currentTimeMillis() + ".png");
        page.screenshot(new Page.ScreenshotOptions().setPath(filePath));
        return filePath.toString();
      }
    } catch (PlaywrightException e) {
      // Handle Playwright specific exceptions
      log.error("Error taking screenshot: " + e.getMessage());
      return null;
    } catch (Exception e) {
      // Handle other exceptions
      log.error("An unexpected error occurred: " + e.getMessage());
      return null; 
    }
  }
}
