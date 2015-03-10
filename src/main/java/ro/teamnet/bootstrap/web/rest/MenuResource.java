package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.repository.MenuRepository;
import ro.teamnet.bootstrap.repository.RoleRepository;
import ro.teamnet.bootstrap.service.MenuService;
import ro.teamnet.bootstrap.web.rest.dto.MenuDTO;
import ro.teamnet.bootstrap.web.rest.dto.MenuUpdateDTO;

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

    @Inject
    MenuRepository menuRepository;

    @Inject
    RoleRepository roleRepository;

    @RequestMapping(value = "/rest/menu", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity save(@RequestBody Menu menu) {
        menuRepository.save(menu);
        return new ResponseEntity<>(new MenuDTO(menu), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/menu", method = RequestMethod.DELETE)
    @Timed
    public ResponseEntity delete(@RequestParam("id") Long id) {
        menuRepository.delete(menuRepository.getById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/menu/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getMenu(@PathVariable Long id) {
        List<Menu> menus = menuService.getMenuByParentId(id);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/menu-tree/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getMenuTree(@PathVariable Long id) {
        List<MenuDTO> menus = menuService.getMenuTreeByParentId(id);
        return new ResponseEntity<>(menus, HttpStatus.OK);
    }

    @Transactional
    @Timed
    @RequestMapping(value = "/rest/menu/bulk-update", method = {RequestMethod.POST})
    public void bulkMenuUpdate(@RequestBody List<MenuUpdateDTO> updates) {
        for(MenuUpdateDTO muDTO: updates) {
            if(muDTO.getProperty().equals(MenuUpdateDTO.PropertyTypes.SORT_NO.name())) {
                menuService.updateMenuPosition(muDTO.getId(), muDTO.getParentId(), muDTO.getNewSortNo());
            }
        }
    }

    @RequestMapping(value = "/rest/menu/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity getRoles() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }
}
