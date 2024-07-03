package dev.findfirst.security.userAuth.tenant.data;

import dev.findfirst.security.userAuth.tenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

interface TenantRepository extends JpaRepository<Tenant, Integer> {}
