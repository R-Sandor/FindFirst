package dev.findfirst.core.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RobotsFetcher {

  public RobotsTxtResponse getRobotsTxt(String url) {

    try {
      URI uri = new URI(url);
      URL robotsUrl = new URI(uri.getScheme(), uri.getAuthority(), "/robots.txt", uri.getQuery(),
          uri.getFragment()).toURL();

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

  public record RobotsTxtResponse(int statusCode, byte[] text, String contentType) {

    @Override
    public final boolean equals(Object obj) {
      if (obj != null && obj instanceof RobotsTxtResponse robotTxt) {
        return robotTxt.statusCode() == this.statusCode() && robotTxt.text() == this.text()
            && robotTxt.contentType.equals(this.contentType());
      } else {
        return false;
      }

    }

    @Override
    public final int hashCode() {
      return this.statusCode() + Arrays.hashCode(this.text()) + this.contentType().hashCode();
    }

    @Override
    public final String toString() {
      return String.format("Status: %s, contentType: %s", this.statusCode(), this.contentType());
    }

  }
}
