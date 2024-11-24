package dev.findfirst.security.userauth.utils;

public class Constants {

  private Constants() {}

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_SCHEMA = "Bearer ";
  public static final String USER_ID_CLAIM = "userId";
  public static final String ROLE_ID_CLAIM = "roleId";
  public static final String ROLE_NAME_CLAIM = "roleName";

  public static final String TENANT_FILTER_NAME = "tenantFilter";
  public static final String TENANT_PARAMETER_NAME = "tenantId";
  public static final String TENANT_COLUMN_NAME = "tenant_id";

  public static final String MODERATOR_ROLE = "ROLE_MODERATOR";
  public static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
  public static final String USER_ROLE_NAME = "ROLE_USER";

  public static final int SUPERUSER_ID = -1000;
}
