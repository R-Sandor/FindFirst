package dev.findfirst.bookmarkit.security.tenant.data;

import dev.findfirst.bookmarkit.security.tenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

interface TenantRepository extends JpaRepository<Tenant, Integer> {}
