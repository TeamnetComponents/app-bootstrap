package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.service.MenuService;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing application menus.
 */
@RestController
@RequestMapping("/data")
public class MenuResource {
    @Inject
    MenuService menuService;

    /**
     * GET  /rest/menu/:id -> get the menus with parent "id".
     */
    @RequestMapping(value = "/rest/menu/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getMenu(@PathVariable Long id) {
        List<Menu> menus = menuService.getMenuByParentId(id);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }
}
