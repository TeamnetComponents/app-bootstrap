package ro.teamnet.bootstrap.dictionary.repository;

import ro.teamnet.bootstrap.dictionary.domain.DictionaryElement;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Spring Data JPA repository for the DictionaryElement entity.
 */
public interface DictionaryElementRepository extends AppRepository<DictionaryElement, Long> {
}
