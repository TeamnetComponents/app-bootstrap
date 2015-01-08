package ro.teamnet.bootstrap.dictionary.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.dictionary.domain.Dictionary;
import ro.teamnet.bootstrap.dictionary.repository.DictionaryRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * REST controller for managing Dictionary.
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryResource {

    private final Logger log = LoggerFactory.getLogger(DictionaryResource.class);

    @Inject
    private DictionaryRepository dictionaryRepository;

    /**
     * POST  /rest/dictionaries -> Create a new dictionary.
     */
    @RequestMapping(value = "/rest/dictionaries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Dictionary dictionary) {
        log.debug("REST request to save Dictionary : {}", dictionary);
        dictionaryRepository.save(dictionary);
    }

    /**
     * GET  /rest/dictionaries -> get all the dictionaries.
     */
    @RequestMapping(value = "/rest/dictionaries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dictionary> getAll() {
        log.debug("REST request to get all dictionaries");
        return dictionaryRepository.findAll();
    }

    /**
     * GET  /rest/dictionaries/:id -> get the "id" dictionary.
     */
    @RequestMapping(value = "/rest/dictionaries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dictionary> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Dictionary : {}", id);
        Dictionary dictionary = dictionaryRepository.findOne(id);
        if (dictionary == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dictionary, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/dictionaries/:id -> delete the "id" dictionary.
     */
    @RequestMapping(value = "/rest/dictionaries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Dictionary : {}", id);
        dictionaryRepository.delete(id);
    }
}
