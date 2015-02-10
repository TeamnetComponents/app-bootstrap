package ro.teamnet.bootstrap.service;

import ro.teamnet.bootstrap.domain.Menu;
import ro.teamnet.bootstrap.web.rest.dto.MenuDTO;

import java.util.List;

public interface MenuService {
    List<Menu> getMenuByParentId(Long parentId);
    List<MenuDTO> getMenuTreeByParentId(Long parentId);
    void updateMenuPosition(Long id, Long parentId, Long sortNo);
}
