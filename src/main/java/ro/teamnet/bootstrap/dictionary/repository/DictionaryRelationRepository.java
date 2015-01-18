package ro.teamnet.bootstrap.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.teamnet.bootstrap.dictionary.domain.DictionaryRelation;

/**
 * Spring Data JPA repository for the DictionaryRelation entity.
 */
public interface DictionaryRelationRepository extends JpaRepository<DictionaryRelation, Long> {

}
