package dev.findfirst.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import dev.findfirst.core.service.TypesenseService;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TypesenseTestConfig {
  @Bean
  public TypesenseService typesenseService() {
    TypesenseService mockService = mock(TypesenseService.class);
    when(mockService.createSchema()).thenReturn("Mocked Schema Creation");
    return mockService;
  }
}
