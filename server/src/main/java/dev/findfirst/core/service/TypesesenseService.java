package dev.findfirst.core.service;

import jakarta.annotation.PostConstruct;
import dev.findfirst.core.model.jdbc.TypesenseInitRecord;
import dev.findfirst.core.repository.jdbc.TypsenseInitializationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionResponse;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypesesenseService {

  private final TypsenseInitializationRepository initRepo;

  private final Client client;

  @PostConstruct
  String createSchema() {
    var q = initRepo.findByScriptName("init");

    CollectionSchema collectionSchema = new CollectionSchema();
    collectionSchema.name("bookmark")
        .addFieldsItem(new Field().name("title").type(FieldTypes.STRING))
        .addFieldsItem(new Field().name("text").type(FieldTypes.STRING));

    CollectionResponse collectionResponse;

    if (q == null) {
      var initRecord = new TypesenseInitRecord(null, "init", null, false, new Date());
      initRepo.save(initRecord);
      try {
        collectionResponse = client.collections().create(collectionSchema);
        log.debug(collectionResponse.toString());
        initRecord.setInitialized(true);
        initRepo.save(initRecord);
        return "Successful initialization";
      } catch (Exception e) {
        log.error(e.toString());
        return "failed";
      }
    } else if (!q.isInitialized()) {
      try {
        collectionResponse = client.collections().create(collectionSchema);
        log.debug(collectionResponse.toString());
        q.setInitialized(true);
        initRepo.save(q);
        return "Successful initialization";

      } catch (Exception e) {
        log.error(e.toString());
        return "failed";
      }
    }
    return "Already initialized";
  }
}
