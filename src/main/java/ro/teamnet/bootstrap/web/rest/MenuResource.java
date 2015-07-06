package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.service.MenuService;
import ro.teamnet.bootstrap.web.rest.dto.MenuDTO;
import ro.teamnet.bootstrap.web.rest.dto.MenuUpdateDTO;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing application menus.
 */
@RestController
@RequestMapping("/app/rest/menu")
public class MenuResource extends AbstractResource<Menu,Long>{

    private final MenuService menuService;

    @Inject
    public MenuResource(MenuService abstractService) {
        super(abstractService);
        this.menuService=abstractService;
    }

    @Override
    public MenuService getService() {
        return (MenuService) super.getService();
    }

    @RequestMapping(value = "/dto", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public MenuDTO createMenu(@RequestBody Menu menu) {
        return getService().create(menu);
    }

    @RequestMapping(value = "/dto", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public MenuDTO updateMenu(@RequestBody Menu menu) {
        return getService().create(menu);
    }


    @RequestMapping(value = "/children/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getMenu(@PathVariable Long id) {
        List<Menu> menus = menuService.getMenuByParentId(id);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getMenuTree(@PathVariable Long id) {
        List<MenuDTO> menus = menuService.getMenuTreeByParentId(id);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @Timed
    @RequestMapping(value = "/bulkUpdate", method = {RequestMethod.PUT})
    public void bulkMenuUpdate(@RequestBody List<MenuUpdateDTO> updates) {
        menuService.bulkUpdate(updates);
    }
}
