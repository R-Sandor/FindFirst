package dev.findfirst.users.service;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import dev.findfirst.users.controller.UserController;
import dev.findfirst.users.model.user.User;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

@Ignore
class UserServiceTest {

  @Mock
  private UserManagementService userManagementService;

  @InjectMocks
  private UserController userController;

  public UserServiceTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUploadProfilePicture_Success() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.uploadProfilePicture(file);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("File uploaded successfully.", response.getBody());
    verify(userManagementService, times(1)).changeUserPhoto(eq(user), file);
  }

  @Test
  void testRemoveUserPhoto_Success() {
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
  void testUploadProfilePicture_FileSizeExceedsLimit() throws Exception {
    byte[] largeContent = new byte[3 * 1024 * 1024]; // 3 MB
    MockMultipartFile file = new MockMultipartFile("file", "large.jpg", "image/jpeg", largeContent);
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.uploadProfilePicture(file);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("File size exceeds the maximum limit of 2 MB.", response.getBody());
  }

  @Test
  void testUploadProfilePicture_InvalidFileType() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());
    int userId = 1;

    User user = new User();
    user.setUserId(userId);
    user.setUsername("testUser");
    when(userManagementService.getUserById(userId)).thenReturn(Optional.of(user));

    ResponseEntity<?> response = userController.uploadProfilePicture(file);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid file type. Only JPG and PNG are allowed.", response.getBody());
  }

  @Test
  void testGetUserProfilePicture_NotFound() {
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
  void testGetUserProfilePicture_Success() {
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
