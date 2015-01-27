package ro.teamnet.bootstrap.dictionary.repository;

import ro.teamnet.bootstrap.dictionary.domain.Dictionary;
import ro.teamnet.bootstrap.extend.AppRepository;

/**
 * Spring Data JPA repository for the Dictionary entity.
 */
public interface DictionaryRepository extends AppRepository<Dictionary, Long> {
}
