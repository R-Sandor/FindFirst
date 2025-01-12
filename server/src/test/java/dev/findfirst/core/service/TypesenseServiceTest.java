package dev.findfirst.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.typesense.api.ApiCall;
import org.typesense.api.Client;
import org.typesense.api.Collections;
import org.typesense.api.Configuration;
import org.typesense.api.FieldTypes;
import org.typesense.model.CollectionResponse;
import org.typesense.model.CollectionSchema;
import org.typesense.model.Field;
import org.typesense.resources.Node;

import dev.findfirst.core.model.jdbc.TypesenseInitRecord;
import dev.findfirst.core.repository.jdbc.TypsenseInitializationRepository;

/**
 * Tests for typsense operations such as intitilization, queries/imports.
 */
@ExtendWith(MockitoExtension.class)
class TypesenseServiceTest {
  @Mock
  private TypsenseInitializationRepository typesenseRepo;

  @InjectMocks
  private TypesesenseService typesense;

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
    var d = new Date();

    var qRecord = new TypesenseInitRecord(1, "init", null, false, d);
    when(typesenseRepo.findByScriptName("init")).thenReturn(null);
    when(typesenseRepo.save(any(TypesenseInitRecord.class))).thenReturn(qRecord);

    when(client.collections()).thenReturn(collections);
    when(collections.create(any(CollectionSchema.class))).thenReturn(collectionResponse);
    var status = typesense.createSchema();
    assertEquals("Successful initialization", status);
  }
  // Use the file to test typesense service.
  // Mock saving the initialized file.
}
