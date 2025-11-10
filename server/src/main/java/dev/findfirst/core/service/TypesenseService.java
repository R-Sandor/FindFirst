package dev.findfirst.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jakarta.annotation.PostConstruct;

import dev.findfirst.core.model.jdbc.BookmarkJDBC;
import dev.findfirst.core.model.jdbc.TypesenseInitRecord;
import dev.findfirst.core.repository.jdbc.TypsenseInitializationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionResponse;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;
import org.typesense.model.SearchParameters;
import org.typesense.model.SearchResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypesenseService {

  public record SearchHighlightResult(Long id, String highlight) {
  };

  private final TypsenseInitializationRepository initRepo;

  private final Client client;

  private final static String schemaName = "bookmark";

  @PostConstruct
  public String createSchema() {
    var q = initRepo.findByScriptName("init");
    if (q == null) {
      var initRecord = new TypesenseInitRecord(null, "init", null, false, new Date());
      return saveSchema(initRecord);
    } else if (!q.isInitialized()) {
      return saveSchema(q);
    }
    return "Already initialized";
  }

  private CollectionSchema createCollectionSchemaSchema() {
    CollectionSchema collectionSchema = new CollectionSchema();
    collectionSchema.name(schemaName)
        .addFieldsItem(new Field().name("title").type(FieldTypes.STRING))
        .addFieldsItem(new Field().name("text").type(FieldTypes.STRING));
    return collectionSchema;
  }

  private String saveSchema(TypesenseInitRecord initRecord) {
    try {
      CollectionResponse collectionResponse = client.collections().create(createCollectionSchemaSchema());
      log.debug(collectionResponse.toString());
      initRecord.setInitialized(true);
      initRepo.save(initRecord);
      return "Successful initialization";
    } catch (Exception e) {
      log.error(e.toString());
      return "failed";
    }

  }

  public void addText(BookmarkJDBC bookmark, Document retDoc) {
    log.debug("Adding text to typesense");
    if (retDoc == null) {
      return;
    }
    HashMap<String, Object> document = new HashMap<>();
    document.put("id", bookmark.getId().toString());
    document.put("title", bookmark.getTitle());
    // lazy dump the document.
    document.put("text", retDoc.text());

    try {
      client.collections(schemaName).documents().create(document);
    } catch (Exception e) {
      log.error(e.toString());
    }
  }

  public List<SearchHighlightResult> search(String text) {
    SearchParameters searchParameters = new SearchParameters().q(text).queryBy("text")
        .highlightFields("text").highlightStartTag("<mark>").highlightEndTag("</mark>");
    try {
      log.debug("searching");
      SearchResult searchResult = client.collections(schemaName).documents().search(searchParameters);
      log.debug(searchResult.toString());

      return searchResult.getHits().stream().map(hit -> {
        Long id = Long.parseLong(hit.getDocument().get("id").toString());

        String highlight = hit.getHighlights().stream().filter(h -> "text".equals(h.getField()))
            .map(h -> h.getSnippet()).findFirst().orElse("");
        return new SearchHighlightResult(id, highlight);
      }).toList();
    } catch (Exception e) {
      log.error(e.toString());
    }
    return List.of();
  }

}
