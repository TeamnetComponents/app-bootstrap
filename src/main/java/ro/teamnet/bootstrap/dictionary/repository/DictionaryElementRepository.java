package ro.teamnet.bootstrap.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.teamnet.bootstrap.dictionary.domain.DictionaryElement;

/**
 * Spring Data JPA repository for the DictionaryElement entity.
 */
public interface DictionaryElementRepository extends JpaRepository<DictionaryElement, Long> {
}
