package dev.findfirst.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.userauth.context.UserContext;
import dev.findfirst.users.controller.UserController;
import dev.findfirst.users.model.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserManagementService userManagementService;

  @Mock
  private UserContext userContext;

  @Mock
  private RegistrationService registrationService;

  @Mock
  private ForgotPasswordService forgotPasswordService;

  @Mock
  private RefreshTokenService refreshTokenService;

  @InjectMocks
  private UserController userController;

  @Captor
  private ArgumentCaptor<User> userArgumentCaptor;

  private User mockUser;

  @BeforeEach
  void setUp() {
    mockUser = new User();
    mockUser.setUserId(1);
    mockUser.setUsername("testUser");
    mockUser.setUserPhoto("uploads/profile-pictures/test.jpg");

    // Mocking the allowedTypes field
    ReflectionTestUtils.setField(userController, "allowedTypes",
        new String[] {"image/jpeg", "image/png"});
  }


  @Test
  void testGetUserProfilePicture_NotFound() {

    mockUser.setUserPhoto(null);

    when(userManagementService.getUserById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

    ResponseEntity<?> response = userController.getUserProfilePicture(mockUser.getUserId());

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testGetUserProfilePicture_Success() throws IOException {

    // Create a temporary file to simulate an existing user photo
    File tempFile = File.createTempFile("test", ".jpg");
    String tempFilePath = tempFile.getAbsolutePath();

    assertTrue(tempFile.exists());

    mockUser.setUserPhoto(tempFilePath);

    when(userManagementService.getUserById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

    ResponseEntity<?> response = userController.getUserProfilePicture(mockUser.getUserId());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}
