package ro.teamnet.bootstrap.service;

import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.web.rest.dto.MenuDTO;
import ro.teamnet.bootstrap.web.rest.dto.MenuUpdateDTO;

import java.util.List;

public interface MenuService extends AbstractService<Menu, Long> {
    List<Menu> getMenuByParentId(Long parentId);
    List<MenuDTO> getMenuTreeByParentId(Long parentId);
    void updateMenuPosition(Long id, Long parentId, Long sortNo);

    MenuDTO create(Menu menu);

    void bulkUpdate(List<MenuUpdateDTO> updates);
}
