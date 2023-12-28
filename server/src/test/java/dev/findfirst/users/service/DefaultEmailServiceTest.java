package dev.findfirst.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.findfirst.core.annotations.IntegrationTest;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.webservices.client.WebServiceClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@IntegrationTest
// @SpringBootTest
@WebServiceClientTest(DefaultEmailService.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class DefaultEmailServiceTest {

  @Value("${spring.mail.port}") int port;

  @Value("${spring.mail.host}") String host;

  @Autowired DefaultEmailService emailService;

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @Container
  public static GenericContainer<?> mailhog =
      new GenericContainer<>(DockerImageName.parse("mailhog/mailhog:latest"))
          .withExposedPorts(1025);

  @TestConfiguration
  public static class JavaMailSenderConfiguration {
    @Bean
    public JavaMailSender javaMailSender() {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

      mailSender.setHost(mailhog.getHost());
      mailSender.setPort(mailhog.getFirstMappedPort());
      mailSender.setProtocol("smtp");

      Properties properties = new Properties();
      properties.setProperty("mail.smtp.auth", "false");
      properties.setProperty("mail.smtp.starttls.enable", "false");

      mailSender.setJavaMailProperties(properties);

      return mailSender;
    }
  }

  @Test
  void portsShouldBeSetEmailShouldSend() {
    assertEquals("localhost", host);
    assertEquals(1025, port);

    emailService.sendSimpleEmail("jacob@test.com", "Sending an email", "Message body here");
  }
}
