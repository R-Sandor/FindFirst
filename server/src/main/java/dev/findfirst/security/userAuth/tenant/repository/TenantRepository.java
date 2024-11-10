package dev.findfirst.security.userAuth.tenant.repository;

import dev.findfirst.security.userAuth.tenant.model.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Integer> {
}
