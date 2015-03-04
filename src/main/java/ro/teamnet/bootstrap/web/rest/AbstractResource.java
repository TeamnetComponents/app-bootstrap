package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.teamnet.bootstrap.service.AbstractService;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;


public abstract class AbstractResource<T extends Serializable, ID extends Serializable> {

    private final Logger log = LoggerFactory.getLogger(AbstractResource.class);


    public AbstractService<T, ID> abstractService;

    public AbstractResource(AbstractService<T, ID> abstractService) {
        this.abstractService = abstractService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Timed
    public void create(@RequestBody T t) {
        log.debug("REST request to save : {}", t);
        abstractService.save(t);
    }


    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
        public List<T> getAll() {
        log.debug("REST request to get all fields");
        return abstractService.findAll();
    }



    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<T> get(@PathVariable ID id, HttpServletResponse response) {
        log.debug("REST request to get  : {}", id);
        T t = abstractService.findOne(id);
        if (t == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(t, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable ID id) {
        log.debug("REST request to delete : {}", id);
        abstractService.delete(id);
    }

}
