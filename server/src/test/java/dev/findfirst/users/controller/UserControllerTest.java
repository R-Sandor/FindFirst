package dev.findfirst.users.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.security.userAuth.models.payload.request.SignupRequest;
import dev.findfirst.users.model.MailHogMessage;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
public class UserControllerTest {

  @Autowired TestRestTemplate restTemplate;

  @Container @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

  @Container
  public static GenericContainer<?> mailhog =
      new GenericContainer<>(DockerImageName.parse("mailhog/mailhog:latest"))
          .withExposedPorts(1025, 8025);

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

  private String userUrl = "/user";

  /**
   * Tests that a user should be able to sign up. After signing up another user should not be able
   * use the same username or email.
   */
  @Test
  void userSignup() {
    var headers = new HttpHeaders();
    var ent =
        new HttpEntity<>(
            new SignupRequest(
                "Steve-Man", "steve@test.com", "Stephen Hayes", "$tev3s_sup3rH@rdPassword"),
            headers);
    var response = restTemplate.exchange(userUrl + "/signup", HttpMethod.POST, ent, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    /** This should fail as the user should already exist. */
    response = restTemplate.exchange(userUrl + "/signup", HttpMethod.POST, ent, String.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  /**
   * Create a user, gets the registration token from the email. Uses the token to complete
   * registration.
   */
  @Test
  void completeRegistration() {
    var headers = new HttpHeaders();
    var ent =
        new HttpEntity<>(
            new SignupRequest(
                "beardedMan", "j-dog@gmail.com", "James Johnson", "$tev3s_sup3rH@rdPassword"),
            headers);
    var response = restTemplate.exchange(userUrl + "/signup", HttpMethod.POST, ent, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    try {
      var token = getTokenFromEmail();
      var regResponse =
          restTemplate.getForEntity(
              userUrl + "/regitrationConfirm?token={token}", String.class, token);
      // assertEquals(HttpStatus.OK, regResponse.getStatusCode());
      assertEquals(HttpStatus.SEE_OTHER, regResponse.getStatusCode());

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getTokenFromEmail() throws Exception {
    String host = mailhog.getHost();
    int port = mailhog.getMappedPort(8025);

    String url = "http://" + host + ":" + port + "/api/v2/messages";
    // String response = Request.Get(url).execute().returnContent().asString();
    var messageRaw = restTemplate.getForEntity(url, String.class).getBody();

    ObjectMapper mapper = new ObjectMapper();
    MailHogMessage mailHogMessage = mapper.readValue(messageRaw, MailHogMessage.class);
    var message = mailHogMessage.items()[0];
    var body = message.Content().Body();
    var secondLine = body.split("\n")[1];
    var token = secondLine.split("=")[1];
    return token.strip();
  }
}
