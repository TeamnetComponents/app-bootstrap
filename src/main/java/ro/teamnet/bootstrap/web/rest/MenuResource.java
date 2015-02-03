package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.service.MenuService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/data")
public class MenuResource {
    private final Logger log = LoggerFactory.getLogger(MenuResource.class);

    @Inject
    MenuService menuService;

    /**
     * GET  /rest/menu/:id -> get the menus with parent "id".
     */
    @RequestMapping(value = "/rest/menu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getMenus() {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority a: authorities) {
            log.info(a.getAuthority());
        }

        List<Menu> menus = menuService.getMenuByParentId(0L);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    /**
     * GET  /rest/menu/:id -> get the menus with parent "id".
     */
    @RequestMapping(value = "/rest/menu/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getUser(@PathVariable Long id) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(GrantedAuthority a: authorities) {
            log.info(a.getAuthority());
        }

        List<Menu> menus = menuService.getMenuByParentId(id);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }
}
