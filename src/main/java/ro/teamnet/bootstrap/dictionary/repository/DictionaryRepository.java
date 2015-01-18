package ro.teamnet.bootstrap.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.teamnet.bootstrap.dictionary.domain.Dictionary;

/**
 * Spring Data JPA repository for the Dictionary entity.
 */
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
}
