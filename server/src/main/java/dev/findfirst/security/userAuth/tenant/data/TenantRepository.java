package dev.findfirst.security.userAuth.tenant.data;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.findfirst.security.userAuth.tenant.model.Tenant;

interface TenantRepository extends JpaRepository<Tenant, Integer> {
}
