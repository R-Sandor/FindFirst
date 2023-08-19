package dev.findfirst.bookmarkit.security.utils;

public class Constants {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_SCHEMA = "Bearer ";
  public static final String TENANT_ID_CLAIM = "tenantId";
  public static final String ROLE_ID_CLAIM = "roleId";
  public static final String ROLE_NAME_CLAIM = "roleName";

  public static final String TENANT_FILTER_NAME = "tenantFilter";
  public static final String TENANT_PARAMETER_NAME = "tenantId";
  public static final String TENANT_COLUMN_NAME = "tenant_id";

  public static final int SUPER_ADMIN_ROLE_ID = 0;
  public static final String SUPER_ADMIN_ROLE_NAME = "ROLE_SUPER_ADMIN";
  public static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
  public static final String USER_ROLE_NAME = "ROLE_USER";

  public static final int SUPER_TENANT_ADMINISTRATOR_ID = 0;
}
