package dev.findfirst.bookmarkit.security.tenant.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TenantableRepository<T> extends JpaRepository<T, Long> {

  Optional<T> findOneById(int id);
}
