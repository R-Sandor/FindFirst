package dev.findfirst.security.userAuth.tenant.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import dev.findfirst.security.userAuth.tenant.model.Tenant;

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
