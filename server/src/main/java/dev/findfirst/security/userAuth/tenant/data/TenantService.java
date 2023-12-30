package dev.findfirst.security.userAuth.tenant.data;

import dev.findfirst.security.userAuth.tenant.model.Tenant;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

  @Autowired private TenantRepository tenantRepository;

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
