package ro.teamnet.bootstrap.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.teamnet.bootstrap.dictionary.domain.DictionaryValue;

/**
 * Spring Data JPA repository for the DictionaryValue entity.
 */
public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Long> {
}
