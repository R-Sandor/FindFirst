package dev.findfirst.users.service;

import org.springframework.beans.factory.annotation.Value;

public abstract class AccountService {

  protected final UserManagementService userManagement;

  protected final DefaultEmailService emailService;

  @Value("${findfirst.app.domain}")
  protected String domain;

  public AccountService(UserManagementService service, DefaultEmailService email) {
    this.userManagement = service;
    this.emailService = email;
  }

  abstract void AccountEmailOp(String emailAddres, String token);
}
