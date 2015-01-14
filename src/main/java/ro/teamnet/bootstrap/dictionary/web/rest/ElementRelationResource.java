package ro.teamnet.bootstrap.dictionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.dictionary.domain.ElementRelation;
import ro.teamnet.bootstrap.dictionary.repository.ElementRelationRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing ElementRelation.
 */
@RestController
@RequestMapping("/dictionary")
public class ElementRelationResource {

    private final Logger log = LoggerFactory.getLogger(ElementRelationResource.class);

    @Inject
    private ElementRelationRepository elementRelationRepository;

    /**
     * POST  /elementRelations -> Create a new elementRelation.
     */
    @RequestMapping(value = "/rest/elementRelations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody ElementRelation elementRelation) {
        log.debug("REST request to save ElementRelation : {}", elementRelation);
        elementRelationRepository.save(elementRelation);
    }

    /**
     * GET  /elementRelations -> get all the elementRelations.
     */
    @RequestMapping(value = "/rest/elementRelations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ElementRelation> getAll() {
        log.debug("REST request to get all ElementRelations");
        return elementRelationRepository.findAll();
    }

    /**
     * GET  /elementRelations/:id -> get the "id" elementRelation.
     */
    @RequestMapping(value = "/rest/elementRelations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ElementRelation> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get ElementRelation : {}", id);
        ElementRelation elementRelation = elementRelationRepository.findOne(id);
        if (elementRelation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(elementRelation, HttpStatus.OK);
    }

    /**
     * DELETE  /elementRelations/:id -> delete the "id" elementRelation.
     */
    @RequestMapping(value = "/rest/elementRelations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ElementRelation : {}", id);
        elementRelationRepository.delete(id);
    }
}
