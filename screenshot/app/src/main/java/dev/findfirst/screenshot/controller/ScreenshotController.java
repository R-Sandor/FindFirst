package dev.findfirst.screenshot.controller;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
      log.error(e.toString());
      return false;
    }
  }

  @GetMapping("/screenshot")
  public String takeScreenshot(@RequestParam @NotBlank @Size(min = 4, max = 50) String url)
      throws BadRequestException, MalformedURLException, URISyntaxException {

    if (!isValid(url)) {
      throw new BadRequestException();
    }
    // Use a try-with-resources block to manage the Playwright resources
    try (Playwright playwright = Playwright.create()) {
      // Use Chromium for this example; you can choose another browser type
      BrowserType browserType = playwright.chromium();

      try (Browser browser = browserType.launch();
          BrowserContext context = browser.newContext();
          Page page = context.newPage()) {
        page.navigate(url);

        var validated = new URL(url).toURI();
        url = URLDecoder.decode(validated.toString(), StandardCharsets.UTF_8);
        String cleanUrl = url.replaceAll("https?://", "").replace("/", "_");
        cleanUrl = cleanUrl.replaceAll("[*\"/\\<>:|?]+", "");
        Path filePath = Path.of(screenshotSaveLoc, cleanUrl + ".png");
        page.screenshot(new Page.ScreenshotOptions().setPath(filePath));

        return filePath.getFileName().toString();
      }
    }
  }
}
