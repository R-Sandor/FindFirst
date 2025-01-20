package dev.findfirst.core.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.findfirst.core.service.TypesenseService;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TypesenseTestConfig {
  @Bean
  public TypesenseService typesenseService() {
    TypesenseService mockService = mock(TypesenseService.class);
    when(mockService.createSchema()).thenReturn("Mocked Schema Creation");
    return mockService;
  }
}
