package ro.teamnet.bootstrap.dictionary.repository;

import ro.teamnet.bootstrap.dictionary.domain.DictionaryValue;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Spring Data JPA repository for the DictionaryValue entity.
 */
public interface DictionaryValueRepository extends AppRepository<DictionaryValue, Long> {
}
