package dev.findfirst.security.userAuth.tenant.aspects;

import java.util.Collections;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import dev.findfirst.core.annotations.DisableTenantFilter;
import dev.findfirst.security.jwt.TenantAuthenticationToken;
import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;
import dev.findfirst.security.userAuth.tenant.listeners.TenantEntityListener;
import dev.findfirst.security.userAuth.utils.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantAspect {

  private final TenantContext tenantContext;

  @PersistenceContext
  private EntityManager entityManager;

  @Before("execution(* dev.findfirst.security.userAuth.tenant.repository.TenantableRepository+.find*(..))")
  public void beforeFindOfTenantableRepository(JoinPoint joinPoint) {
    entityManager.unwrap(Session.class).disableFilter(Constants.TENANT_FILTER_NAME);
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    // Enable Tenant Filter only if the method do not have DisableTenantFilter and context user is
    // not super user with id -1000
    if (AnnotationUtils.getAnnotation(methodSignature.getMethod(),
        DisableTenantFilter.class) == null
        && tenantContext.getTenantId() != Constants.SUPERUSER_ID) {
      entityManager.unwrap(Session.class).enableFilter(Constants.TENANT_FILTER_NAME)
          .setParameter(Constants.TENANT_PARAMETER_NAME, tenantContext.getTenantId());
    }
  }

  @Before("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public void setSecurityContextBeforeScheduledTask() {
    // Disable TenantEntityListener which updates tenant_id for entities.
    TenantEntityListener.disableListener();
    log.info("Setting Security context before scheduled task");
    var simpleGrantedAuthority = new SimpleGrantedAuthority("admin");
    var grantedAuthList = Collections.singletonList(simpleGrantedAuthority);
    TenantAuthenticationToken tenantAuthenticationToken = new TenantAuthenticationToken(
        "admin@findfirst.dev", 2, grantedAuthList, Constants.SUPERUSER_ID);
    SecurityContextHolder.getContext().setAuthentication(tenantAuthenticationToken);
    // Adding function that will run after transaction is completed.
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      log.info("Setting listener to clear security context after scheduled task");
      TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
        @Override
        public void afterCompletion(int status) {
          SecurityContextHolder.clearContext();
          TenantEntityListener.enableListener();
        }
      });
    }
  }

}
