package dev.findfirst.screenshot.controller;

import com.microsoft.playwright.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
public class ScreenshotController {

    @Value("${findfirst.screenshot.location:./}")
    private String screenshotSaveLoc;

    @GetMapping("/screenshot")
    public String takeScreenshot(@RequestParam String url) {
        // Use a try-with-resources block to manage the Playwright resources
        try (Playwright playwright = Playwright.create()) {
            // Use Chromium for this example; you can choose another browser type
            BrowserType browserType = playwright.chromium();
            try (Browser browser = browserType.launch()) {
                BrowserContext context = browser.newContext();
                Page page = context.newPage();
                String cleanUrl = url.replace("/", "_");
                Path filePath = Path.of("/app", "screenshots", cleanUrl + System.currentTimeMillis() + ".png");
                page.screenshot(new Page.ScreenshotOptions().setPath(filePath));
                return "Screenshot saved to: " + filePath;
            }
        } catch (PlaywrightException e) {
            // Handle Playwright specific exceptions
            e.printStackTrace();
            return "Error taking screenshot: " + e.getMessage();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return "An unexpected error occurred: " + e.getMessage();
        }
    }
}
