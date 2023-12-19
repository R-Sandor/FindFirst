package dev.findfirst.core.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.core.annotations.IntegrationTestConfig;
import dev.findfirst.core.repository.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DataJpaTest
@Testcontainers
@IntegrationTestConfig
public class UserControllerTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired BookmarkRepository bkmkRepo;

  // @Test
  // public void userShouldHaveBookmarks() {
  //   var results = bkmkRepo.findAll();
  //   assertNotNull(results);
  // }
  @Test
  void test() {
    assertEquals(postgres.isRunning(), true);
    // assertNotNull(bkmkRepo);
  }
}
