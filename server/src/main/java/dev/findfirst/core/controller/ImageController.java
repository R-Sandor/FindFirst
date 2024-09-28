package dev.findfirst.core.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/screenshots")
@Slf4j
public class ImageController {

  @GetMapping("{fileName}")
  public byte[] getImage(@PathVariable String fileName) {

    Path filePath = Path.of("/app/screenshots/" + fileName);
    try {
      return Files.newInputStream(filePath, StandardOpenOption.READ).readAllBytes();
    } catch (IOException ex) {
      log.error(ex.toString());
      return new byte[0];
    }
  }
}
