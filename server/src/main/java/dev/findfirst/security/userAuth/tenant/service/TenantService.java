package dev.findfirst.security.userAuth.tenant.service;

import jakarta.transaction.Transactional;

import dev.findfirst.security.userAuth.tenant.model.Tenant;
import dev.findfirst.security.userAuth.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

  private final TenantRepository tenantRepository;

  public Tenant create(String name) {
    var tenant = new Tenant();
    tenant.setName(name);
    return tenantRepository.save(tenant);
  }

  @Transactional
  public void deleteById(int id) {
    tenantRepository.deleteById(id);
  }
}
