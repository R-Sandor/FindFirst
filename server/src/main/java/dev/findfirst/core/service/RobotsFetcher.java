package dev.findfirst.core.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RobotsFetcher {

  private final RestTemplate rest;

  public RobotsTxtResponse getRobotsTxt(String url) {

    try {
      URI uri = new URI(url);
      URI robotsUri = new URI(uri.getScheme(), uri.getAuthority(), "/robots.txt", uri.getQuery(),
          uri.getFragment());

      ResponseEntity<String> robots = rest.getForEntity(robotsUri, String.class);

      return new RobotsTxtResponse(robots.getStatusCode().value(), robots.getBody().getBytes(),
          robots.getHeaders().getContentType() == null ? ""
              : robots.getHeaders().getContentType().toString());

    } catch (URISyntaxException | HttpClientErrorException ex) {
      log.error(ex.toString());
      return new RobotsTxtResponse(500, "".getBytes(), "");
    }
  }

  public record RobotsTxtResponse(int statusCode, byte[] text, String contentType) {

    @Override
    public final boolean equals(Object obj) {
      if (obj != null && obj instanceof RobotsTxtResponse(int statusCode, byte[] text, String ct)) {
        return statusCode == this.statusCode() && Arrays.equals(this.text(), text)
            && this.contentType.equals(ct);
      } else {
        return false;
      }

    }

    @Override
    public final int hashCode() {
      int contentTypeHash = 0;
      if (contentType() != null) {
        contentTypeHash = contentType.hashCode();
      }
      return this.statusCode + Arrays.hashCode(this.text) + contentTypeHash;
    }

    @Override
    public final String toString() {
      return String.format("Status: %s, contentType: %s, text %s", this.statusCode, this.contentType,
          new String(this.text));
    }

  }
}
