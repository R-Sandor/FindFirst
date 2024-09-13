package dev.findfirst.core.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class ScreenshotManagerTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private ScreenshotManager screenshotManager = new ScreenshotManager();
  

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(screenshotManager, "screenshotServiceUrl", "http://localhost:8080");
  }

  @Test
  public void returnMockedPath() {
    var expectedUrl = "https:__findfirst.dev";
    Mockito
        .when(restTemplate.getForObject(
            "http://localhost:8080/screenshot?url=https://findfirst.dev", String.class))
        .thenReturn("https:__findfirst.dev");
    var pathUrl = screenshotManager.getScreenshot("https://findfirst.dev");

    Assertions.assertEquals(pathUrl, expectedUrl);
  }

}
