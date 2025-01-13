package dev.findfirst.users.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.Properties;

import dev.findfirst.core.annotations.IntegrationTest;
import dev.findfirst.security.userauth.models.TokenRefreshResponse;
import dev.findfirst.security.userauth.models.payload.request.SignupRequest;
import dev.findfirst.users.model.MailHogMessage;
import dev.findfirst.users.model.user.TokenPassword;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.service.UserManagementService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@IntegrationTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
class UserControllerTest {

  final TestRestTemplate restTemplate;

  @Mock
  private UserManagementService userManagementService;

  @Autowired
  UserControllerTest(TestRestTemplate tRestTemplate) {
    this.restTemplate = tRestTemplate;
  }

  private MockMvc mockMvc;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");

  @Container
  public static GenericContainer<?> mailhog =
      new GenericContainer<>(DockerImageName.parse("mailhog/mailhog:latest")).withExposedPorts(1025,
          8025);

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
    var ent = new HttpEntity<>(
        new SignupRequest("Steve-Man", "steve@test.com", "$tev3s_sup3rH@rdPassword"), headers);
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
  @Order(2)
  void completeSignupAndRegistration() {
    var headers = new HttpHeaders();
    var ent = new HttpEntity<>(
        new SignupRequest("beardedMan", "j-dog@gmail.com", "$tev3s_sup3rH@rdPassword"), headers);
    var response = restTemplate.exchange(userUrl + "/signup", HttpMethod.POST, ent, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    try {
      var token = getTokenFromEmail(0, 1);
      var regResponse = restTemplate.getForEntity(userUrl + "/regitrationConfirm?token={token}",
          String.class, token);
      assertEquals(HttpStatus.SEE_OTHER, regResponse.getStatusCode());
    } catch (Exception e) {
      // fail the test should show message
      assertTrue(false, e.getMessage());
    }
  }

  @Test
  @Order(1)
  void resetPassword() {
    String token = "";
    var response = restTemplate.exchange(userUrl + "/resetPassword?email={email}", HttpMethod.POST,
        new HttpEntity<>(new HttpHeaders()), String.class, "jsmith@google.com");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    try {
      token = getTokenFromEmail(0, 2);
    } catch (Exception e) {
      // fail the test should show message
      assertTrue(false, e.getMessage());
    }
    response = restTemplate.exchange(userUrl + "/changePassword?token={tkn}", HttpMethod.GET,
        new HttpEntity<>(new HttpHeaders()), String.class, token);
    assertEquals(HttpStatus.SEE_OTHER, response.getStatusCode());

    var loc = Optional.ofNullable(response.getHeaders().get("Location")).orElseThrow().get(0);
    var urlStruct = loc.split("/");
    // token is the last part of the string
    var tknParam = urlStruct[urlStruct.length - 1];
    assertNotNull(tknParam);
    restTemplate.exchange(userUrl + "/changePassword?tokenPassword={tkn}", HttpMethod.POST,
        new HttpEntity<>(new TokenPassword(tknParam, "jsmithsNewsPassword!"), new HttpHeaders()),
        String.class, token);
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth("jsmith", "jsmithsNewsPassword!");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    var signResp = restTemplate.postForEntity("/user/signin", entity, TokenRefreshResponse.class);
    assertEquals(HttpStatus.OK, signResp.getStatusCode());

  }

  String getTokenFromEmail(int emailIdx, int lineWithToken) throws Exception {
    String host = mailhog.getHost();
    int port = mailhog.getMappedPort(8025);

    String url = "http://" + host + ":" + port + "/api/v2/messages";
    var messageRaw = restTemplate.getForEntity(url, String.class).getBody();

    ObjectMapper mapper = new ObjectMapper();
    MailHogMessage mailHogMessage = mapper.readValue(messageRaw, MailHogMessage.class);
    var message = mailHogMessage.items()[emailIdx];
    var body = message.Content().Body();
    var secondLine = body.split("\n")[lineWithToken];
    var token = secondLine.split("=")[1];
    return token.strip();
  }

  @Test
  void refreshToken() {
    HttpHeaders headers = new HttpHeaders();
    // test user
    headers.setBasicAuth("jsmith", "test");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    var signResp = restTemplate.postForEntity("/user/signin", entity, TokenRefreshResponse.class);
    var tknRefresh = Optional.ofNullable(signResp.getBody()).orElseThrow();
    var refreshTkn = tknRefresh.refreshToken();
    var resp = restTemplate.exchange(userUrl + "/refreshToken?token={refreshToken}",
        HttpMethod.POST, new HttpEntity<>(new HttpHeaders()), String.class, refreshTkn);
    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  public void testUploadProfilePicture_Success() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.uploadProfilePicture(file, userId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("File uploaded successfully.", response.getBody());
    verify(userManagementService, times(1)).changeUserPhoto(eq(user), anyString());
  }

  @Test
  public void testRemoveUserPhoto_Success() {
    User user = new User();
    user.setUserId(1);
    user.setUsername("testUser");
    user.setUserPhoto("uploads/profile-pictures/test.jpg");

    when(userManagementService.getUserById(user.getUserId())).thenReturn(Optional.of(user));

    userManagementService.removeUserPhoto(user);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userManagementService, times(1)).saveUser(userCaptor.capture());
    assertNull(userCaptor.getValue().getUserPhoto());
  }

  @Test
  public void testUploadProfilePicture_FileSizeExceedsLimit() throws Exception {
    byte[] largeContent = new byte[3 * 1024 * 1024]; // 3 MB
    MockMultipartFile file = new MockMultipartFile("file", "large.jpg", "image/jpeg", largeContent);
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.uploadProfilePicture(file, userId);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("File size exceeds the maximum limit of 2 MB.", response.getBody());
  }

  @Test
  public void testUploadProfilePicture_InvalidFileType() throws Exception {
    MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.uploadProfilePicture(file, userId);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid file type. Only JPG and PNG are allowed.", response.getBody());
  }

  @Test
  public void testGetUserProfilePicture_NotFound() {
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    user.setUserPhoto(null);
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.getUserProfilePicture(userId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testGetUserProfilePicture_Success() {
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    user.setUserPhoto("uploads/profile-pictures/test.jpg");
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.getUserProfilePicture(userId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}
