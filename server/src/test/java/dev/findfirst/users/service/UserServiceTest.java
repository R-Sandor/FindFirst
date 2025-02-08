package dev.findfirst.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import dev.findfirst.security.jwt.service.RefreshTokenService;
import dev.findfirst.security.userauth.context.UserContext;
import dev.findfirst.users.controller.UserController;
import dev.findfirst.users.model.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
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
  @Disabled
  void testUploadProfilePicture_Success() throws Exception {

    MockMultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());

    when(userManagementService.getUserById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

    ResponseEntity<?> response = userController.uploadProfilePicture(file);
    System.out.println(response.getBody());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("File uploaded successfully.", response.getBody());

    verify(userManagementService, times(1)).changeUserPhoto(eq(mockUser), eq(file));
  }

  @Test
  @Disabled
  void testRemoveUserPhoto_Success() throws Exception {

    MockMultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());

    when(userManagementService.getUserById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

    ResponseEntity<?> response = userController.uploadProfilePicture(file);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("File uploaded successfully.", response.getBody());

    verify(userManagementService, times(1)).changeUserPhoto(eq(mockUser), eq(file));
    verify(userManagementService, times(1)).removeUserPhoto(eq(mockUser));

    assertNull(mockUser.getUserPhoto());
  }

  @Test
  void testUploadProfilePicture_FileSizeExceedsLimit() throws Exception {

    byte[] largeContent = new byte[3 * 1024 * 1024]; // 3 MB

    MockMultipartFile file = new MockMultipartFile("file", "large.jpg", "image/jpeg", largeContent);

    ResponseEntity<?> response = userController.uploadProfilePicture(file);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("File size exceeds the maximum limit of 2 MB.", response.getBody());

  }

  @Test
  void testUploadProfilePicture_InvalidFileType() throws Exception {

    MockMultipartFile file =
        new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());

    ResponseEntity<?> response = userController.uploadProfilePicture(file);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid file type. Only JPG and PNG are allowed.", response.getBody());
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
