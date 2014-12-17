package ro.teamnet.bootstrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.teamnet.bootstrap.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
