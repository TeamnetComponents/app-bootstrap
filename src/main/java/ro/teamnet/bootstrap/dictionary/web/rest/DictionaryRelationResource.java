package ro.teamnet.bootstrap.dictionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.dictionary.domain.DictionaryRelation;
import ro.teamnet.bootstrap.dictionary.repository.DictionaryRelationRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing DictionaryRelation.
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryRelationResource {

    private final Logger log = LoggerFactory.getLogger(DictionaryRelationResource.class);

    @Inject
    private DictionaryRelationRepository dictionaryRelationRepository;

    /**
     * POST  /dictionaryRelations -> Create a new dictionaryRelation.
     */
    @RequestMapping(value = "/rest/dictionaryRelations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody DictionaryRelation dictionaryRelation) {
        log.debug("REST request to save DictionaryRelation : {}", dictionaryRelation);
        dictionaryRelationRepository.save(dictionaryRelation);
    }

    /**
     * GET  /dictionaryRelations -> get all the dictionaryRelations.
     */
    @RequestMapping(value = "/rest/dictionaryRelations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DictionaryRelation> getAll() {
        log.debug("REST request to get all DictionaryRelations");
        return dictionaryRelationRepository.findAll();
    }

    /**
     * GET  /dictionaryRelations/:id -> get the "id" dictionaryRelation.
     */
    @RequestMapping(value = "/rest/dictionaryRelations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictionaryRelation> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get DictionaryRelation : {}", id);
        DictionaryRelation dictionaryRelation = dictionaryRelationRepository.findOne(id);
        if (dictionaryRelation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dictionaryRelation, HttpStatus.OK);
    }

    /**
     * DELETE  /dictionaryRelations/:id -> delete the "id" dictionaryRelation.
     */
    @RequestMapping(value = "/rest/dictionaryRelations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete DictionaryRelation : {}", id);
        dictionaryRelationRepository.delete(id);
    }
}
