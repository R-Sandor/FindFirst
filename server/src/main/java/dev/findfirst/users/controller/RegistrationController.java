package dev.findfirst.users.controller;

import dev.findfirst.security.userAuth.execeptions.NoVerificationTokenFoundException;
import dev.findfirst.security.userAuth.execeptions.TokenExpiredException;
import dev.findfirst.security.userAuth.models.payload.request.SignupRequest;
import dev.findfirst.security.userAuth.models.payload.response.MessageResponse;
import dev.findfirst.security.userAuth.tenant.data.TenantService;
import dev.findfirst.users.model.user.URole;
import dev.findfirst.users.model.user.User;
import dev.findfirst.users.repository.RoleRepository;
import dev.findfirst.users.service.RegistrationService;
import dev.findfirst.users.service.UserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
  private final UserService userService;

  private final PasswordEncoder passwdEncoder;

  private final RoleRepository roleRepository;

  private final TenantService tenantService;

  private final RegistrationService regService;
  
  @Value("${bookmarkit.app.frontend-url:http://localhost:3000/}") 
  private String frontendUrl;

  @GetMapping("api/regitrationConfirm")
  public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) throws URISyntaxException {

    HttpHeaders httpHeaders = new HttpHeaders();
    URI findfirst = new URI(frontendUrl);
    try {
      regService.registrationComplete(token);
    } catch (NoVerificationTokenFoundException | TokenExpiredException e) {
      return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }
    httpHeaders.setLocation(findfirst);
    return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
  }


  @PostMapping("api/auth/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userService.getUserExistByUsername(signUpRequest.username())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userService.getUserExistEmail(signUpRequest.email())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest, passwdEncoder.encode(signUpRequest.password()));
    var role =
        roleRepository.findById(URole.ROLE_USER.ordinal()).orElseThrow(NoSuchElementException::new);
    user.setRole(role);
    var t = tenantService.create(signUpRequest.username());
    // create a new tenant
    try {
      user.setTenantId(t.getId());
      userService.saveUser(user);
    } catch (Exception e) {
      // If any exception occurs we should delete the records that were just made.
      tenantService.deleteById(t.getId());
      userService.deleteUser(user);
      return ResponseEntity.badRequest().body(new MessageResponse("Could not signup, try again."));
    }
    regService.sendRegistration(user);
    return ResponseEntity.ok(new MessageResponse("User Account Created, Complete Registration!"));
  }
}
