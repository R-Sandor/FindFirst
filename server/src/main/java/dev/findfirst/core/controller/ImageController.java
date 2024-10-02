package dev.findfirst.core.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/screenshots")
@Slf4j
public class ImageController {

  @Value("${findfirst.screenshot.location}") private String screenshotSaveLoc;

  @GetMapping("{fileName}")
  public byte[] getImage(@PathVariable String fileName) {
    Path filePath = Path.of(screenshotSaveLoc, fileName);
    if (Files.exists(filePath)) {
      try {
        return Files.newInputStream(filePath, StandardOpenOption.READ).readAllBytes();
      } catch (IOException ex) {
        log.error(ex.toString());
        return new byte[0];
      }
    }
    return new byte[0];
  }
}
