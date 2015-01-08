package ro.teamnet.bootstrap.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.teamnet.bootstrap.dictionary.domain.DictionaryCollection;

/**
 * Spring Data JPA repository for the DictionaryCollection entity.
 */
public interface DictionaryCollectionRepository extends JpaRepository<DictionaryCollection, Long> {
}
