package dev.findfirst.core.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RobotsFetcher {

  public RobotsTxtResponse getRobotsTxt(String url) {

    try {
      URL urlObj = new URI(url).toURL();
      URL robotsUrl =
          new URL(urlObj.getProtocol(), urlObj.getHost(), urlObj.getPort(), "/robots.txt");
      HttpURLConnection conn = (HttpURLConnection) robotsUrl.openConnection();
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(2000);
      conn.setReadTimeout(2000);
      int statusCode = conn.getResponseCode();
      byte[] robotsContent = conn.getInputStream().readAllBytes();
      String contentType = conn.getContentType();
      conn.disconnect();
      return new RobotsTxtResponse(statusCode, robotsContent, contentType);

    } catch (URISyntaxException | IOException ex) {
      log.error(ex.toString());
      return new RobotsTxtResponse(500, "".getBytes(), "");
    }
  }

  public record RobotsTxtResponse(int statusCode, byte[] text, String contentType) {}
}
