package ro.teamnet.bootstrap.service;

import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.web.rest.dto.MenuDTO;
import ro.teamnet.bootstrap.web.rest.dto.MenuUpdateDTO;

import java.util.List;

/**
 * Service class used for menu implementation.
 */
public interface MenuService extends AbstractService<Menu, Long> {

    /**
     *
     * @param parentId - id of parent menu
     * @return a list of menu children
     */
    List<Menu> getMenuByParentId(Long parentId);

    /**
     *
     * @param parentId - id of parent menu
     * @return
     */
    List<MenuDTO> getMenuTreeByParentId(Long parentId);

    /**
     *
     * @param id
     * @param parentId - id of parent menu
     * @param sortNo
     */
    void updateMenuPosition(Long id, Long parentId, Long sortNo);

    /**
     * Create a MenuDTO from a given menu.
     * @param menu
     * @return a MenuDTO
     */
    MenuDTO create(Menu menu);

    /**
     *
     * @param updates
     */
    void bulkUpdate(List<MenuUpdateDTO> updates);
}
