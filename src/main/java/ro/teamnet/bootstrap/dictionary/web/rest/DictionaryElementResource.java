package ro.teamnet.bootstrap.dictionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.dictionary.domain.DictionaryElement;
import ro.teamnet.bootstrap.dictionary.repository.DictionaryElementRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing dictionary element.
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryElementResource {
    private final Logger log = LoggerFactory.getLogger(DictionaryElementResource.class);

    @Inject
    private DictionaryElementRepository dictionaryElementRepository;

    /**
     * POST  /rest/dictionaryElements -> Create a new dictionary element.
     */
    @RequestMapping(value = "/rest/dictionaryElements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody DictionaryElement dictionaryElement) {
        log.debug("REST request to save DictionaryElement : {}", dictionaryElement);
        dictionaryElementRepository.save(dictionaryElement);
    }

    /**
     * GET  /rest/dictionaryElements -> get all the dictionary elements.
     */
    @RequestMapping(value = "/rest/dictionaryElements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DictionaryElement> getAll() {
        log.debug("REST request to get all DictionaryElements");
        return dictionaryElementRepository.findAll();
    }

    /**
     * GET  /rest/dictionaryElements/:id -> get the "id" dictionary element.
     */
    @RequestMapping(value = "/rest/dictionaryElements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DictionaryElement> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get DictionaryElement : {}", id);
        DictionaryElement dictionaryElement = dictionaryElementRepository.findOne(id);
        if (dictionaryElement == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dictionaryElement, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/dictionaryElements/:id -> delete the "id" dictionary element.
     */
    @RequestMapping(value = "/rest/dictionaryElements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete DictionaryElement : {}", id);
        dictionaryElementRepository.delete(id);
    }
}
