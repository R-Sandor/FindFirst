package dev.findfirst.users.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import dev.findfirst.users.model.oauth2.Oauth2Source;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

class Oauth2SourceServiceTest {

  private Oauth2SourceService service;

  private ClientRegistration provider;

  @BeforeEach
  void setUp() {
    provider = ClientRegistration.withRegistrationId("test").clientName("Test Provider")
        .authorizationUri("https://www.example-auth.com")
        .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
        .tokenUri("https://www.example.com").build();
    var providers = new InMemoryClientRegistrationRepository(List.of(provider));
    service = new Oauth2SourceService();
    service.setOauth2Providers(providers);
  }

  @Test
  void testReturnsOauth2SourcesWithIconUrl_WhenFoundedAtWebsite() throws IOException {
    try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
      Connection mockConnection = mock(Connection.class);
      Document mockDoc = mock(Document.class);
      Element mockElement = mock(Element.class);
      Elements mockElements = mock(Elements.class);

      when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
      when(mockConnection.get()).thenReturn(mockDoc);
      when(mockDoc.head()).thenReturn(mockElement);
      when(mockElement.select(anyString())).thenReturn(mockElements);
      when(mockElements.first()).thenReturn(mockElement);
      when(mockElement.attr("href")).thenReturn("https://example.com/assets/favicon.ico");
      jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);

      service.init();

      List<Oauth2Source> oauth2Sources = service.oauth2Sources();

      assertFalse(oauth2Sources.isEmpty());
      var oauth2Source = oauth2Sources.getFirst();
      assertEquals(provider.getClientName(), oauth2Source.provider());
      assertEquals("https://example.com/assets/favicon.ico", oauth2Source.iconUrl());
      assertEquals("oauth2/authorization/" + provider.getRegistrationId(),
          oauth2Source.authEndpoint());


    }
  }

  @Test
  void testReturnsOauth2SourcesWithoutIconUrl_WhenNotFoundedAtWebsite() throws IOException {
    try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
      Connection mockConnection = mock(Connection.class);
      Document mockDoc = mock(Document.class);
      Element mockElement = mock(Element.class);
      Elements mockElements = mock(Elements.class);

      when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
      when(mockConnection.get()).thenReturn(mockDoc);
      when(mockDoc.head()).thenReturn(mockElement);
      when(mockElement.select(anyString())).thenReturn(mockElements);
      when(mockElements.first()).thenReturn(null);
      jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);

      service.init();

      List<Oauth2Source> oauth2Sources = service.oauth2Sources();

      assertFalse(oauth2Sources.isEmpty());
      var oauth2Source = oauth2Sources.getFirst();
      assertEquals(provider.getClientName(), oauth2Source.provider());
      assertNull(oauth2Source.iconUrl());
      assertEquals("oauth2/authorization/" + provider.getRegistrationId(),
          oauth2Source.authEndpoint());


    }
  }

  @Test
  void testReturnsOauth2SourcesWithoutIconUrl_WhenNotConnectedAtWebsite() throws IOException {
    try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
      Connection mockConnection = mock(Connection.class);
      when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
      when(mockConnection.get()).thenThrow(IOException.class);

      jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);

      service.init();

      List<Oauth2Source> oauth2Sources = service.oauth2Sources();

      assertFalse(oauth2Sources.isEmpty());
      var oauth2Source = oauth2Sources.getFirst();
      assertEquals(provider.getClientName(), oauth2Source.provider());
      assertNull(oauth2Source.iconUrl());
      assertEquals("oauth2/authorization/" + provider.getRegistrationId(),
          oauth2Source.authEndpoint());

    }
  }

  @Test
  void testReturnsOauth2SourcesWithoutUntrustedTokenUri() {
    provider = ClientRegistration.withRegistrationId("test").clientName("Test Provider")
        .authorizationUri("https://www.example-auth.com")
        .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
        .tokenUri("http://www.example.com").build();
    var providersWithoutSSL = new InMemoryClientRegistrationRepository(List.of(provider));
    service.setOauth2Providers(providersWithoutSSL);
    service.init();
    assertTrue(service.oauth2Sources().isEmpty());

  }
}
