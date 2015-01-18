package ro.teamnet.bootstrap.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.teamnet.bootstrap.dictionary.domain.ElementRelation;

/**
 * Spring Data JPA repository for the ElementRelation entity.
 */
public interface ElementRelationRepository extends JpaRepository<ElementRelation,Long>{

}
