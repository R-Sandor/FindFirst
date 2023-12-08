package dev.findfirst.security.tenant.data;

import dev.findfirst.security.tenant.model.Tenant;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

  @Autowired private TenantRepository tenantRepository;

  // @Autowired
  // private JwtService jwtService;

  public Tenant create(Tenant tenant) {
    return tenantRepository.save(tenant);
  }

  public Tenant create(String name) {
    var tenant = new Tenant();
    tenant.setName(name);
    return tenantRepository.save(tenant);
  }

  public List<Tenant> findAll() {
    return tenantRepository.findAll();
  }

  public Tenant updateById(int id, Tenant tenant) {
    tenantRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    tenant.setId(id);
    return tenantRepository.save(tenant);
  }

  @Transactional
  public void deleteById(int id) {
    tenantRepository.deleteById(id);
  }

  // public String impersonate(int tenantId) {
  //     String email = (String)
  // SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  //     return jwtService.createSuperAdminJwt(email, tenantId);
  // }
}
