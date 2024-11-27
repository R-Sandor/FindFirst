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

  @Value("${findfirst.screenshot.location}")
  private String screenshotSaveLoc;

  @GetMapping("{fileName}")
  public byte[] getImage(@PathVariable String fileName) throws IOException {
    Path filePath = Path.of(screenshotSaveLoc, fileName);
    if (Files.exists(filePath)) {
      return Files.newInputStream(filePath, StandardOpenOption.READ).readAllBytes();
    }
    return new byte[0];
  }
}
