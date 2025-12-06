package dev.findfirst.core.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;
import dev.findfirst.core.model.jdbc.TypesenseInitRecord;
import dev.findfirst.core.repository.jdbc.TypsenseInitializationRepository;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.typesense.api.Client;
import org.typesense.api.Collection;
import org.typesense.api.Collections;
import org.typesense.api.Documents;
import org.typesense.model.CollectionResponse;
import org.typesense.model.CollectionSchema;
import org.typesense.model.SearchHighlight;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;
import org.typesense.model.SearchResultHit;

/**
 * Tests for typsense operations such as intitilization, queries/imports.
 */
@ExtendWith(MockitoExtension.class)
class TypesenseServiceTest {
  @Mock
  private TypsenseInitializationRepository typesenseRepo;

  @InjectMocks
  private TypesenseService typesense;

  @Mock
  Client client;

  @Mock
  Date date;

  @Mock
  private Collections collections;

  @Mock
  private CollectionResponse collectionResponse;

  @Test
  void alreadyInitialized() {
    when(typesenseRepo.findByScriptName("init")).thenReturn(new TypesenseInitRecord(1, "init", null, true, new Date()));
    var status = typesense.createSchema();
    assertEquals("Already initialized", status);
  }

  @Test
  void initializeRecord() throws Exception {
    var qRecord = new TypesenseInitRecord(1, "init", null, false, new Date());
    when(typesenseRepo.findByScriptName("init")).thenReturn(null);
    when(typesenseRepo.save(any(TypesenseInitRecord.class))).thenReturn(qRecord);

    when(client.collections()).thenReturn(collections);
    when(collections.create(any(CollectionSchema.class))).thenReturn(collectionResponse);
    var status = typesense.createSchema();
    assertEquals("Successful initialization", status);
  }

  @Test
  void initializationWasNotFinished() throws Exception {
    var qRecord = new TypesenseInitRecord(1, "init", null, false, new Date());
    var postInitialization = new TypesenseInitRecord(1, "init", null, true, new Date());
    when(typesenseRepo.findByScriptName("init")).thenReturn(qRecord);
    when(typesenseRepo.save(any(TypesenseInitRecord.class))).thenReturn(postInitialization);

    when(client.collections()).thenReturn(collections);
    when(collections.create(any(CollectionSchema.class))).thenReturn(collectionResponse);
    var status = typesense.createSchema();
    assertEquals("Successful initialization", status);
  }

  @Test
  void storeScrapedText() throws Exception {

    BookmarkJDBC bkmk = new BookmarkJDBC(1l, 1, new Date(), "Test user", "Test user", new Date(),
        "Dancing with wolves", "https://example.com", "", true, null);

    Document doc = mock(Document.class);
    HashMap<String, Object> document = new HashMap<>();
    document.put("id", bkmk.getId().toString());
    document.put("title", bkmk.getTitle());
    // lazy dump the document.
    document.put("text", doc.text());
    var collection = mock(Collection.class);
    var documents = mock(Documents.class);
    when(client.collections("bookmark")).thenReturn(collection);
    when(collection.documents()).thenReturn(documents);
    when(client.collections("bookmark").documents().create(document)).thenReturn(new HashMap<>());
    assertDoesNotThrow(() -> typesense.addText(bkmk, doc));
  }

  @Test
  void returnSearchHighlighted() throws Exception {

    String query = "example";
    Long expectedId = 123L;
    String expectedSnippet = "<mark>example</mark> text around";

    // Mock highlight
    SearchHighlight highlight = mock(SearchHighlight.class);
    when(highlight.getField()).thenReturn("text");
    when(highlight.getSnippet()).thenReturn(expectedSnippet);

    // Simula un hit como Map
    SearchResultHit hit = mock(SearchResultHit.class);
    when(hit.getDocument()).thenReturn(Map.of("id", expectedId.toString()));
    when(hit.getHighlights()).thenReturn(List.of(highlight));

    // Mock search result
    SearchResult searchResult = new SearchResult();
    searchResult.setHits(List.of(hit));

    // Mock client behavior
    var documents = mock(Documents.class);
    var collection = mock(Collection.class);
    when(client.collections("bookmark")).thenReturn(collection);
    when(collection.documents()).thenReturn(documents);
    when(documents.search(any(SearchParameters.class))).thenReturn(searchResult);

    // Act
    var results = typesense.search(query);

    // Assert
    assertEquals(1, results.size());
    assertEquals(expectedId, results.get(0).id());
    assertEquals(expectedSnippet, results.get(0).highlight());
  }
}
