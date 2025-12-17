package dev.findfirst.users.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import dev.findfirst.users.model.oauth2.Oauth2Source;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2SourceService {

  private InMemoryClientRegistrationRepository oauth2Providers;

  private final List<Oauth2Source> oauth2Sources = new ArrayList<>();

  @PostConstruct
  void init() {
    if (oauth2Providers != null) {
      oauth2Providers.iterator().forEachRemaining(provider -> {
        var tknUri = provider.getProviderDetails().getTokenUri();
        log.debug("Token URI {}", tknUri);
        // skip http(s)://
        if (!tknUri.contains("https://")) {
          log.debug("provider without https {}", tknUri);
          // do we really want to trust anything that isn't https?
          return;
        }
        oauth2Sources.add(new Oauth2Source(provider.getClientName(), getFaviconURI(provider),
            "oauth2/authorization/" + provider.getRegistrationId()));
      });
    }
  }

  public List<Oauth2Source> oauth2Sources() {
    return oauth2Sources;
  }

  @Autowired(required = false)
  public void setOauth2Providers(InMemoryClientRegistrationRepository oauth2Providers) {
    this.oauth2Providers = oauth2Providers;
  }

  private String getFaviconURI(ClientRegistration provider) {
    var targetUri = provider.getProviderDetails().getAuthorizationUri();
    log.debug("Scraping {} for favicon URI", targetUri);
    try {
      Element link = Jsoup.connect(targetUri).userAgent("Mozilla").get().head()
          .select("link[href~=.*\\.(ico|png)]").first();

      if (link == null) {
        log.debug("No favicon URI found at {}", targetUri);
        return null;
      }

      String href = link.attr("href");
      log.debug("Found favicon URI: {}", href);
      return href;
    } catch (IOException e) {
      log.error("Failed to scrape {}", targetUri, e);
      return null;
    }

  }

}
