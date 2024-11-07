package dev.findfirst.screenshot.controller;

import com.microsoft.playwright.*;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

  /* Returns true if url is valid */
  public static boolean isValid(String url) {
    /* Try creating a valid URL */
    try {
      new URL(url).toURI();
      return true;
    }

    // If there was an Exception
    // while creating URL object
    catch (Exception e) {
      return false;
    }
  }

  @GetMapping("/screenshot")
  public String takeScreenshot(
      @RequestParam String url) {

    if (!isValid(url)) { 
      return null;
    }
    // Use a try-with-resources block to manage the Playwright resources
    log.debug("Recieving request");
    try (Playwright playwright = Playwright.create()) {
      // Use Chromium for this example; you can choose another browser type
      BrowserType browserType = playwright.chromium();

      try (Browser browser = browserType.launch()) {
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        page.navigate(url);

        var validated = new URL(url).toURI();
        url = URLDecoder.decode(validated.toString(), StandardCharsets.UTF_8);
        String cleanUrl = url.replaceAll("https?://", "").replace("/", "_");
        cleanUrl = cleanUrl.replaceAll("[*\"/\\<>:|?]+", "");

        Path filePath = Path.of(screenshotSaveLoc, cleanUrl + ".png");
        page.screenshot(new Page.ScreenshotOptions().setPath(filePath));

        return filePath.getFileName().toString();
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
