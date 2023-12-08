package dev.findfirst.security.tenant.data;

import dev.findfirst.security.tenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

interface TenantRepository extends JpaRepository<Tenant, Integer> {}
